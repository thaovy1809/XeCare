"use client"

import { DashboardLayout } from "@/components/dashboard-layout"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Textarea } from "@/components/ui/textarea"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { useState } from "react"
import { Search, Star, Eye, Check, X, Flag, MessageSquare } from "lucide-react"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { useEffect } from "react"
import { MoreHorizontal, MapPin, Building } from "lucide-react"
import { Toaster } from "react-hot-toast"
import toast from "react-hot-toast"
import { format } from 'date-fns';
export interface ReviewDto {
  id: number;
  appointmentId: number;
  userId: number;
  userName?: string;
  garageId?: number;
  garageName?: string;
  rating: number;
  comment?: string;
  createdAt: string;
  status: number;
}


export default function AdminReviewsPage() {
  const [reviews, setReviews] = useState<ReviewDto[]>([]);

  const [searchTerm, setSearchTerm] = useState("")
  const [statusFilter, setStatusFilter] = useState("all")
  const [ratingFilter, setRatingFilter] = useState("all")
  const [selectedReview, setSelectedReview] = useState<any>(null)

  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchGarages = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/v1/reviews")
      if (!res.ok) throw new Error("Failed to fetch users")
      const data = await res.json()
      setReviews(data)
    } catch (err: any) {
      setError(err.message || "Lỗi không xác định")
    } finally {
      setLoading(false)
    }
  }

  const fetchReviews = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/v1/reviews")
      if (!res.ok) throw new Error("Failed to fetch garages")
      const data = await res.json()
      setReviews(data)
    } catch (err: any) {
      setError(err.message || "Lỗi không xác định")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    const fetchReviews = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/v1/reviews")
        if (!res.ok) throw new Error("Failed to fetch garages")
        const data = await res.json()
        setReviews(data)
      } catch (err: any) {
        setError(err.message || "Lỗi không xác định")
      } finally {
        setLoading(false)
      }
    }

    fetchReviews()
  }, [])

  const filteredReviews = reviews.filter((review) => {
    const lowerSearchTerm = searchTerm.toLowerCase();

    const matchesSearch =
      (review.userName?.toLowerCase().includes(lowerSearchTerm) ?? false) ||
      (review.garageName?.toLowerCase().includes(lowerSearchTerm) ?? false) ||
      (review.comment?.toLowerCase().includes(lowerSearchTerm) ?? false);

    const matchesStatus =
      statusFilter === "all" || review.status?.toString() === statusFilter;

    const matchesRating =
      ratingFilter === "all" || review.rating.toString() === ratingFilter;

    return matchesSearch && matchesStatus && matchesRating;
  });


  const getStatusBadge = (status: number) => {
    switch (status) {
      case 1:
        return <Badge className="bg-green-100 text-green-700">Đã duyệt</Badge>
      case -1:
        return <Badge className="bg-yellow-100 text-yellow-700">Chờ duyệt</Badge>
      case 0:
        return <Badge className="bg-red-100 text-red-700">Từ chối</Badge>
      default:
        return <Badge className="bg-gray-100 text-gray-700">Không xác định</Badge>
    }
  }

  const renderStars = (rating: number) => {
    return Array.from({ length: 5 }, (_, i) => (
      <Star key={i} className={`h-4 w-4 ${i < rating ? "text-yellow-400 fill-current" : "text-gray-300"}`} />
    ))
  }

  const handleSetStatus = async (reviewId: number, status: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/v1/reviews/${reviewId}/set-status`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ status }),
      });

      if (!response.ok) {
        throw new Error('Failed to set review status');
      }
      if(status === 1) {
        toast.success("Duyệt đánh giá thành công!")
      } else {
        toast.success("Từ chối đánh giá thành công!")
      }

      fetchReviews();
    } catch (error) {
      console.error('Error setting review status:', error);
    }
  };


  const stats = {
    total: reviews.length,
    approved: reviews.filter((r) => r.status === 1).length,
    pending: reviews.filter((r) => r.status === -1).length,
    reported: reviews.filter((r) => r.status == 0).length,
  }

  return (
    <DashboardLayout
      allowedRoles={["admin"]}
      title="Quản lý đánh giá"
      description="Kiểm duyệt và quản lý đánh giá từ người dùng"
    >
      {/* Stats Cards */}
      <div className="grid md:grid-cols-4 gap-6 mb-6">
        <Card className="border-blue-100">
          <CardContent className="p-6">
            <div className="text-center">
              <p className="text-2xl font-bold text-blue-600">{stats.total}</p>
              <p className="text-sm text-slate-600">Tổng đánh giá</p>
            </div>
          </CardContent>
        </Card>
        <Card className="border-green-100">
          <CardContent className="p-6">
            <div className="text-center">
              <p className="text-2xl font-bold text-green-600">{stats.approved}</p>
              <p className="text-sm text-slate-600">Đã duyệt</p>
            </div>
          </CardContent>
        </Card>
        <Card className="border-yellow-100">
          <CardContent className="p-6">
            <div className="text-center">
              <p className="text-2xl font-bold text-yellow-600">{stats.pending}</p>
              <p className="text-sm text-slate-600">Chờ duyệt</p>
            </div>
          </CardContent>
        </Card>
        <Card className="border-red-100">
          <CardContent className="p-6">
            <div className="text-center">
              <p className="text-2xl font-bold text-red-600">{stats.reported}</p>
              <p className="text-sm text-slate-600">Bị báo cáo</p>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Filters and Table */}
      <Card className="border-blue-100">
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <MessageSquare className="h-5 w-5 text-blue-600" />
            <span>Danh sách đánh giá</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex flex-col sm:flex-row gap-4 mb-6">
            {/* Search */}
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-slate-400" />
              <Input
                placeholder="Tìm kiếm theo người dùng, garage hoặc nội dung..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>

            {/* Status Filter */}
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-full sm:w-[180px]">
                <SelectValue placeholder="Lọc theo trạng thái" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tất cả trạng thái</SelectItem>
                <SelectItem value="approved">Đã duyệt</SelectItem>
                <SelectItem value="pending">Chờ duyệt</SelectItem>
                <SelectItem value="rejected">Từ chối</SelectItem>
              </SelectContent>
            </Select>

            {/* Rating Filter */}
            <Select value={ratingFilter} onValueChange={setRatingFilter}>
              <SelectTrigger className="w-full sm:w-[180px]">
                <SelectValue placeholder="Lọc theo sao" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tất cả đánh giá</SelectItem>
                <SelectItem value="5">5 sao</SelectItem>
                <SelectItem value="4">4 sao</SelectItem>
                <SelectItem value="3">3 sao</SelectItem>
                <SelectItem value="2">2 sao</SelectItem>
                <SelectItem value="1">1 sao</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* Reviews Table */}
          <div className="border rounded-lg">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Người đánh giá</TableHead>
                  <TableHead>Garage</TableHead>
                  <TableHead>Đánh giá</TableHead>
                  <TableHead>Nội dung</TableHead>
                  <TableHead>Trạng thái</TableHead>
                  <TableHead className="text-right">Thao tác</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredReviews.map((review) => (
                  <TableRow key={review.id}>
                    <TableCell>
                      <div>
                        <p className="font-medium text-slate-900">{review.userName}</p>
                        <p className="text-sm text-slate-500">{format(new Date(review.createdAt), 'dd/MM/yyyy')}</p>
                      </div>
                    </TableCell>
                    <TableCell>
                      <p className="font-medium text-slate-700">{review.garageName}</p>
                    </TableCell>
                    <TableCell>
                      <p className="font-medium text-slate-700">{review.comment}</p>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center space-x-1">
                        {renderStars(review.rating)}
                        <span className="ml-2 text-sm font-medium">{review.rating}/5</span>
                      </div>
                    </TableCell>

                    <TableCell>{getStatusBadge(review.status)}</TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end space-x-2">
                        <Dialog>
                          <DialogTrigger asChild>
                            <Button size="sm" variant="outline" onClick={() => setSelectedReview(review)}>
                              <Eye className="h-4 w-4" />
                            </Button>
                          </DialogTrigger>
                          <DialogContent className="max-w-2xl">
                            <DialogHeader>
                              <DialogTitle>Chi tiết đánh giá</DialogTitle>
                            </DialogHeader>
                            {selectedReview && (
                              <div className="space-y-4">
                                <div className="grid grid-cols-2 gap-4">
                                  <div>
                                    <p className="text-sm font-medium text-slate-700">Người đánh giá:</p>
                                    <p className="text-slate-900">{selectedReview.userName}</p>
                                  </div>
                                  <div>
                                    <p className="text-sm font-medium text-slate-700">Garage:</p>
                                    <p className="text-slate-900">{selectedReview.garageName}</p>
                                  </div>
                                  <div>
                                    <p className="text-sm font-medium text-slate-700">Đánh giá:</p>
                                    <div className="flex items-center space-x-1">
                                      {renderStars(selectedReview.rating)}
                                      <span className="ml-2">{selectedReview.rating}/5</span>
                                    </div>
                                  </div>
                                  <div>
                                    <p className="text-sm font-medium text-slate-700">Ngày:</p>
                                    <p className="text-slate-900">
                                      {format(new Date(selectedReview.createdAt), 'dd/MM/yyyy')}
                                    </p>
                                  </div>
                                </div>
                                <div>
                                  <p className="text-sm font-medium text-slate-700 mb-2">Nội dung:</p>
                                  <Textarea value={selectedReview.comment} readOnly className="min-h-[100px]" />
                                </div>
                              </div>
                            )}
                          </DialogContent>
                        </Dialog>

                        {review.status === -1 && (
                          <>
                            <Button
                              size="sm"
                              onClick={() => handleSetStatus(review.id, 1)}
                              className="bg-green-600 hover:bg-green-700"
                            >
                              <Check className="h-4 w-4 mr-1" />
                              
                            </Button>

                            <Button
                              size="sm"
                              variant="destructive"
                              onClick={() => handleSetStatus(review.id, 0)}
                            >
                              <X className="h-4 w-4 mr-1" />
                              
                            </Button>
                          </>
                        )}

                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          {filteredReviews.length === 0 && (
            <div className="text-center py-8">
              <p className="text-slate-500">Không tìm thấy đánh giá nào</p>
            </div>
          )}
        </CardContent>
      </Card>
      <Toaster position="top-right" />
    </DashboardLayout>
  )
}
