"use client"

import { DashboardLayout } from "@/components/dashboard-layout"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { useState, useEffect } from "react"
import { Search, MoreHorizontal, Eye, Check, X, Star, MapPin, Building } from "lucide-react"
import { Toaster } from "react-hot-toast"
import toast from "react-hot-toast"
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog"

import {
  Lock,
  Unlock,
  Trash2,
  UserPlus,
  Download,
  Pencil,
} from "lucide-react"
export interface GarageResponseDto {
  id: number;
  name: string;
  description: string;
  address: string;
  phone: string;
  email: string;
  latitude: number;
  longitude: number;
  rating: number;
  reviewCount: number;
  openTime: string;
  closeTime: string;
  imageUrl: string;
  isVerified: boolean;
  status: 'ACTIVE' | 'INACTIVE' | 'PENDING' | string;
  createdAt: string;
  ownerId: number;
  ownerName: string;
  ownerEmail: string;

  services: GarageServiceDto[];

  vehicleTypes: GarageVehicleTypeDto[];
}

export interface Service {
  id: number;
  name: string;
  description?: string;
  isActive?: boolean;
}

export interface VehicleType {
  id: number;
  name: string;
  description?: string;
  isActive?: boolean;
}

export interface GarageServiceDto {
  id: number;
  serviceId: number;
  serviceName: string;
  serviceDescription: string;
  basePrice: number;
  estimatedTimeMinutes: number;
  isActive: boolean;
}

export interface GarageVehicleTypeDto {
  id: number;
  vehicleTypeId: number;
  vehicleTypeName: string;
  vehicleTypeDescription: string;
  isActive: boolean;
}


export default function AdminGaragesPage() {
  const [garages, setGarages] = useState<GarageResponseDto[]>([]);
  const [servicesList, setServicesList] = useState<Service[]>([]);
  const [vehicleTypesList, setVehicleTypesList] = useState<VehicleType[]>([]);
  const [searchTerm, setSearchTerm] = useState("")
  const [statusFilter, setStatusFilter] = useState("all")
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [selectedGarageReviews, setSelectedGarageReviews] = useState<any[]>([]);
  const [isReviewModalOpen, setIsReviewModalOpen] = useState(false);
  const [reviewModalError, setReviewModalError] = useState<string | null>(null);

  const [formData, setFormData] = useState({
    name: "",
    description: "",
    address: "",
    phone: "",
    email: "",
    latitude: "",
    longitude: "",
    openTime: "",
    closeTime: "",
    serviceIds: [],
    vehicleTypeIds: [],
  });


  const handleMultiSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const { name, options } = e.target;
    const selectedValues = Array.from(options)
      .filter(option => option.selected)
      .map(option => Number(option.value));

    setFormData(prev => ({
      ...prev,
      [name]: selectedValues,
    }));
  };

  const parseTime = (timeString: string | null) => {
    if (!timeString) return "";
    return timeString.length >= 5 ? timeString.slice(0, 5) : timeString;
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleSubmit = async () => {
    setFormLoading(true)
    setFormError(null)
    try {
      const payload = {
        ...formData,
        openTime: parseTime(formData.openTime),
        closeTime: parseTime(formData.closeTime),
      }

      const res = await fetch("http://localhost:8080/api/v1/garages/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      })

      if (!res.ok) {
        const errData = await res.json()
        throw new Error(errData.message || "Failed to create garage")
      }

      fetchGarages();
      toast.success("Thêm thành công 🎉")

      setIsModalOpen(false)
      setFormData({
        name: "",
        description: "",
        address: "",
        phone: "",
        email: "",
        latitude: "",
        longitude: "",
        openTime: "",
        closeTime: "",
        serviceIds: [],
        vehicleTypeIds: [],
      })
    } catch (err: any) {
      setFormError(err.message || "Lỗi không xác định")
    } finally {
      setFormLoading(false)
    }
  }

  const filteredGarages = garages.filter((garage) => {
    const matchesSearch =
      garage.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      garage.address.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesStatus = statusFilter === "all" || garage.status === statusFilter

    return matchesSearch && matchesStatus
  })

  const fetchGarages = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/v1/garages")
      if (!res.ok) throw new Error("Failed to fetch users")
      const data = await res.json()
      setGarages(data)
    } catch (err: any) {
      setError(err.message || "Lỗi không xác định")
    } finally {
      setLoading(false)
    }
  }
  const [isEditModalOpen, setIsEditModalOpen] = useState(false)
  const [editFormData, setEditFormData] = useState({
    id: "",
    name: "",
    description: "",
    address: "",
    phone: "",
    email: "",
    latitude: "",
    longitude: "",
    openTime: "",
    closeTime: "",
    serviceIds: [],
    vehicleTypeIds: [],
  })
  const [editFormLoading, setEditFormLoading] = useState(false)
  const [editFormError, setEditFormError] = useState<string | null>(null)

  const openEditModal = (garage: any) => {
    fetchGarageById(garage.id);
    setEditFormError(null)
    setIsEditModalOpen(true)
  }

  const openReviewModal = (garage: any) => {
    fetchGarageReviewById(garage.id);
  };
  const handleEditInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setEditFormData(prev => ({ ...prev, [name]: value }))
  }


  const handleEditSubmit = async () => {
    setEditFormLoading(true)
    setEditFormError(null)

    try {
      const res = await fetch(`http://localhost:8080/api/v1/garages/${editFormData.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(editFormData),
      })
      if (!res.ok) throw new Error("Cập nhật thất bại")

      toast.success("Cập nhật garage thành công 🎉")
      fetchGarages()
      setIsEditModalOpen(false)
    } catch (error: any) {
      setEditFormError(error.message || "Lỗi không xác định")
    } finally {
      setEditFormLoading(false)
    }
  }
  const handleToggleStatus = async (garageId: number) => {
    try {
      const res = await fetch(`http://localhost:8080/api/v1/garages/${garageId}/toggle-status`, {
        method: "PUT",
      });
      if (!res.ok) throw new Error("Không thể thay đổi trạng thái garage");

      const updatedGarage = await res.json();
      toast.success("Thay đổi trạng thái thành công 🎉")

      setGarages((prev) =>
        prev.map((u) => (u.id === garageId ? updatedGarage : u))
      );
    } catch (err: any) {
      console.error("Toggle status failed", err.message);
    }
  };

  const fetchGarageById = async (id: string) => {
    try {
      const res = await fetch(`http://localhost:8080/api/v1/garages/${id}`);
      console.log(id)
      if (!res.ok) throw new Error("Không tìm thấy garage có id " + id);

      const garage = await res.json();
      console.log(garage)

      setEditFormData({
        id: garage.id || "",
        name: garage.name || "",
        description: garage.description || "",
        address: garage.address || "",
        phone: garage.phone || "",
        email: garage.email || "",
        latitude: garage.latitude || "",
        longitude: garage.longitude || "",
        openTime: garage.openTime || "",
        closeTime: garage.closeTime || "",
        serviceIds: garage.services?.map((s: any) => s.serviceId) || [],
        vehicleTypeIds: garage.vehicleTypes?.map((v: any) => v.vehicleTypeId) || [],
      });

      setEditFormError(null);
      setIsEditModalOpen(true);
    } catch (error: any) {
      setEditFormError(error.message || "Lỗi khi tải dữ liệu garage");
    }
  };

  const fetchGarageReviewById = async (garageId: string) => {
    try {
      const res = await fetch(`http://localhost:8080/api/v1/reviews?id=${garageId}`);
      if (!res.ok) throw new Error("Không tìm thấy đánh giá");

      const reviews = await res.json();
      setSelectedGarageReviews(reviews);
      setReviewModalError(null);

      console.log("✅ Đã fetch review, mở modal");
      setIsReviewModalOpen(true);
    } catch (error: any) {
      console.error("❌ Lỗi khi fetch review:", error);
      setReviewModalError(error.message || "Lỗi khi tải đánh giá");
      setIsReviewModalOpen(true);
    }
  };

  const [formLoading, setFormLoading] = useState(false)
  const [formError, setFormError] = useState<string | null>(null)
  useEffect(() => {
    fetch("http://localhost:8080/api/v1/services")
      .then(res => res.json())
      .then(data => setServicesList(data))
      .catch(err => console.error("Failed to fetch services:", err));

    fetch("http://localhost:8080/api/v1/vehicle-types")
      .then(res => res.json())
      .then(data => setVehicleTypesList(data))
      .catch(err => console.error("Failed to fetch vehicle types:", err));

    const fetchGarages = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/v1/garages")
        if (!res.ok) throw new Error("Failed to fetch garages")
        const data = await res.json()
        setGarages(data)
      } catch (err: any) {
        setError(err.message || "Lỗi không xác định")
      } finally {
        setLoading(false)
      }
    }

    fetchGarages()
  }, [])

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "ACTIVE":
        return <Badge className="bg-green-100 text-green-700">Hoạt động</Badge>
      case "INACTIVE":
        return <Badge className="bg-red-100 text-red-700">Bị khóa</Badge>
      case "PENDING":
        return <Badge className="bg-yellow-100 text-yellow-700">Chờ duyệt</Badge>
      default:
        return <Badge className="bg-gray-100 text-gray-700">Không xác định</Badge>
    }
  }

  const handleApprove = (garageId: number) => {
    setGarages(garages.map((garage) => (garage.id === garageId ? { ...garage, status: "ACTIVE" } : garage)))
  }

  const handleReject = (garageId: number) => {
    if (confirm("Bạn có chắc chắn muốn huỷ garage này?")) {
      setGarages(garages.map((garage) => (garage.id === garageId ? { ...garage, status: "INACTIVE" } : garage)))
    }
  }

  const stats = {
    total: garages.length,
    active: garages.filter((g) => g.status === "ACTIVE").length,
    pending: garages.filter((g) => g.status === "PENDING").length,
    inactive: garages.filter((g) => g.status === "INACTIVE").length,
  }

  return (
    <DashboardLayout
      allowedRoles={["admin"]}
      title="Quản lý garage"
      description="Duyệt và quản lý các garage trong hệ thống"
    >
      {/* Stats Cards */}
      <div className="grid md:grid-cols-4 gap-6 mb-6">
        <Card className="border-blue-100">
          <CardContent className="p-6">
            <div className="text-center">
              <p className="text-2xl font-bold text-blue-600">{stats.total}</p>
              <p className="text-sm text-slate-600">Tổng garage</p>
            </div>
          </CardContent>
        </Card>
        <Card className="border-green-100">
          <CardContent className="p-6">
            <div className="text-center">
              <p className="text-2xl font-bold text-green-600">{stats.active}</p>
              <p className="text-sm text-slate-600">Đang hoạt động</p>
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
              <p className="text-2xl font-bold text-red-600">{stats.inactive}</p>
              <p className="text-sm text-slate-600">Bị khóa</p>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Filters and Table */}
      <Card className="border-blue-100">
        <CardHeader>
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
            <CardTitle>Danh sách garage</CardTitle>
            <div className="flex gap-2">
              <Button
                className="bg-gradient-to-r from-blue-600 to-cyan-600"
                onClick={() => setIsModalOpen(true)}
              >
                <UserPlus className="h-4 w-4 mr-2" />
                Thêm garage
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="flex flex-col sm:flex-row gap-4 mb-6">
            {/* Search */}
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-slate-400" />
              <Input
                placeholder="Tìm kiếm theo tên hoặc địa chỉ..."
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
                <SelectItem value="ACTIVE">Hoạt động</SelectItem>
                <SelectItem value="PENDING">Chờ duyệt</SelectItem>
                <SelectItem value="INACTIVE">Bị khóa</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* Garages Table */}
          <div className="border rounded-lg">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Garage</TableHead>
                  <TableHead>Địa chỉ</TableHead>
                  <TableHead>Đánh giá</TableHead>
                  <TableHead>Dịch vụ</TableHead>
                  <TableHead>Trạng thái</TableHead>
                  <TableHead className="text-right">Thao tác</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredGarages.map((garage) => (
                  <TableRow key={garage.id}>
                    <TableCell>
                      <div>
                        <p className="font-medium text-slate-900">{garage.name}</p>
                        <p className="text-sm text-slate-500">{garage.email}</p>
                        <p className="text-sm text-slate-500">{garage.phone}</p>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-start space-x-1">
                        <MapPin className="h-4 w-4 text-slate-400 mt-0.5 flex-shrink-0" />
                        <span className="text-sm text-slate-600">{garage.address}</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center space-x-1">
                        <Star className="h-4 w-4 text-yellow-400 fill-current" />
                        <span className="font-medium">{garage.rating}</span>
                        <span className="text-sm text-slate-500">({garage.reviewCount})</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex flex-wrap gap-1">
                        {garage.services.slice(0, 2).map((service, index) => (
                          <Badge key={index} variant="secondary" className="text-xs">
                            {service.serviceName}
                          </Badge>
                        ))}
                      </div>
                    </TableCell>
                    <TableCell>{getStatusBadge(garage.status)}</TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end space-x-2">
                        {garage.status === "pending" && (
                          <>
                            <Button
                              size="sm"
                              onClick={() => handleApprove(garage.id)}
                              className="bg-green-600 hover:bg-green-700"
                            >
                              <Check className="h-4 w-4" />
                            </Button>
                            <Button size="sm" variant="destructive" onClick={() => handleReject(garage.id)}>
                              <X className="h-4 w-4" />
                            </Button>
                          </>
                        )}
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" size="sm">
                              <MoreHorizontal className="h-4 w-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end">
                            <DropdownMenuItem onClick={() => openEditModal(garage)}>
                              <Pencil className="h-4 w-4 mr-2" />
                              Chỉnh sửa thông tin
                            </DropdownMenuItem>

                            <DropdownMenuItem onClick={() => handleToggleStatus(garage.id)}>
                              {garage.status === 'ACTIVE' ? (
                                <>
                                  <Lock className="h-4 w-4 mr-2" />
                                  Khoá garage
                                </>
                              ) : (
                                <>
                                  <Unlock className="h-4 w-4 mr-2" />
                                  Kích hoạt garage
                                </>
                              )}
                            </DropdownMenuItem>

                            <DropdownMenuItem onClick={() => openReviewModal(garage)}>
                              <Star className="h-4 w-4 mr-2" />
                              Xem đánh giá
                            </DropdownMenuItem>

                          </DropdownMenuContent>
                        </DropdownMenu>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          {filteredGarages.length === 0 && (
            <div className="text-center py-8">
              <p className="text-slate-500">Không tìm thấy garage nào</p>
            </div>
          )}
        </CardContent>
      </Card>

      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogTrigger asChild>
        </DialogTrigger>
        <DialogContent className="sm:max-w-lg max-h-[600px] overflow-hidden">
          <DialogHeader>
            <DialogTitle>Thêm garage mới</DialogTitle>
            <DialogDescription>Nhập thông tin garage để tạo garage mới.</DialogDescription>
          </DialogHeader>

          <div className="grid gap-4 py-4 overflow-y-auto max-h-[400px]">
            <Input
              name="name"
              placeholder="Tên garage"
              value={formData.name}
              onChange={handleInputChange}
              required
            />
            <Input
              type="email"
              name="email"
              placeholder="Email"
              value={formData.email}
              onChange={handleInputChange}
              required
            />
            <Input
              name="phone"
              placeholder="Số điện thoại"
              value={formData.phone}
              onChange={handleInputChange}
            />
            <Input
              name="address"
              placeholder="Địa chỉ"
              value={formData.address}
              onChange={handleInputChange}
            />

            <Input
              name="latitude"
              placeholder="Vĩ độ"
              value={formData.latitude}
              onChange={handleInputChange}
            />
            <Input
              name="longitude"
              placeholder="Kinh độ"
              value={formData.longitude}
              onChange={handleInputChange}
            />
            <Input
              type="time"
              name="openTime"
              placeholder="Giờ mở cửa"
              value={formData.openTime}
              onChange={handleInputChange}
            />

            <Input
              type="time"
              name="closeTime"
              placeholder="Giờ đóng cửa"
              value={formData.closeTime}
              onChange={handleInputChange}
            />

            <label htmlFor="serviceIds" className="block mb-1 font-medium">
              Chọn dịch vụ
            </label>
            <select
              id="serviceIds"
              name="serviceIds"
              multiple
              value={formData.serviceIds}
              onChange={handleMultiSelectChange}
              className="border rounded p-2 w-full h-32"
            >
              {servicesList.map(service => (
                <option key={service.id} value={service.id}>
                  {service.name}
                </option>
              ))}
            </select>

            <label htmlFor="vehicleTypeIds" className="block mb-1 font-medium mt-4">
              Chọn loại xe
            </label>
            <select
              id="vehicleTypeIds"
              name="vehicleTypeIds"
              multiple
              value={formData.vehicleTypeIds}
              onChange={handleMultiSelectChange}
              className="border rounded p-2 w-full h-32"
            >
              {vehicleTypesList.map(vehicleType => (
                <option key={vehicleType.id} value={vehicleType.id}>
                  {vehicleType.name}
                </option>
              ))}
            </select>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsModalOpen(false)} disabled={formLoading}>
              Hủy
            </Button>
            <Button onClick={handleSubmit} disabled={formLoading}>
              {formLoading ? "Đang tạo..." : "Tạo garage"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={isEditModalOpen} onOpenChange={setIsEditModalOpen}>
        <DialogTrigger asChild></DialogTrigger>
        <DialogContent className="sm:max-w-lg max-h-[600px] overflow-hidden">
          <DialogHeader>
            <DialogTitle>Chỉnh sửa garage</DialogTitle>
            <DialogDescription>Chỉnh sửa thông tin garage.</DialogDescription>
          </DialogHeader>

          <div className="grid gap-4 py-4 overflow-y-auto max-h-[400px]">
            <Input
              name="name"
              placeholder="Tên garage"
              value={editFormData.name}
              onChange={handleEditInputChange}
              required
            />
            <Input
              name="email"
              type="email"
              placeholder="Email"
              value={editFormData.email}
              onChange={handleEditInputChange}
              required
            />
            <Input
              name="phone"
              placeholder="Số điện thoại"
              value={editFormData.phone}
              onChange={handleEditInputChange}
            />
            <Input
              name="address"
              placeholder="Địa chỉ"
              value={editFormData.address}
              onChange={handleEditInputChange}
            />
            <Input
              name="latitude"
              placeholder="Vĩ độ"
              value={editFormData.latitude}
              onChange={handleEditInputChange}
            />
            <Input
              name="longitude"
              placeholder="Kinh độ"
              value={editFormData.longitude}
              onChange={handleEditInputChange}
            />
            <Input
              type="time"
              name="openTime"
              placeholder="Giờ mở cửa"
              value={editFormData.openTime}
              onChange={handleEditInputChange}
            />
            <Input
              type="time"
              name="closeTime"
              placeholder="Giờ đóng cửa"
              value={editFormData.closeTime}
              onChange={handleEditInputChange}
            />

            <label htmlFor="serviceIds" className="block mb-1 font-medium">
              Chọn dịch vụ
            </label>
            <select
              id="serviceIds"
              name="serviceIds"
              multiple
              value={editFormData.serviceIds}
              onChange={(e) => {
                const options = e.target.options
                const values = []
                for (let i = 0; i < options.length; i++) {
                  if (options[i].selected) values.push(Number(options[i].value))
                }
                setEditFormData(prev => ({ ...prev, serviceIds: values }))
              }}
              className="border rounded p-2 w-full h-32"
            >
              {servicesList.map(service => (
                <option key={service.id} value={service.id}>
                  {service.name}
                </option>
              ))}
            </select>

            <label htmlFor="vehicleTypeIds" className="block mb-1 font-medium mt-4">
              Chọn loại xe
            </label>
            <select
              id="vehicleTypeIds"
              name="vehicleTypeIds"
              multiple
              value={editFormData.vehicleTypeIds}
              onChange={(e) => {
                const options = e.target.options
                const values = []
                for (let i = 0; i < options.length; i++) {
                  if (options[i].selected) values.push(Number(options[i].value))
                }
                setEditFormData(prev => ({ ...prev, vehicleTypeIds: values }))
              }}
              className="border rounded p-2 w-full h-32"
            >
              {vehicleTypesList.map(vehicleType => (
                <option key={vehicleType.id} value={vehicleType.id}>
                  {vehicleType.name}
                </option>
              ))}
            </select>
          </div>

          {editFormError && (
            <p className="text-red-600 text-sm mt-2">{editFormError}</p>
          )}

          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => setIsEditModalOpen(false)}
              disabled={editFormLoading}
            >
              Hủy
            </Button>
            <Button onClick={handleEditSubmit} disabled={editFormLoading}>
              {editFormLoading ? "Đang cập nhật..." : "Cập nhật garage"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={isReviewModalOpen} onOpenChange={setIsReviewModalOpen}>
        <DialogContent className="sm:max-w-lg max-h-[600px] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>Đánh giá garage</DialogTitle>
            <DialogDescription>Xem đánh giá từ khách hàng</DialogDescription>
          </DialogHeader>

          {reviewModalError ? (
            <p className="text-red-500">{reviewModalError}</p>
          ) : selectedGarageReviews.length === 0 ? (
            <p>Chưa có đánh giá nào.</p>
          ) : (
            <ul className="space-y-4">
              {selectedGarageReviews.map((review, index) => (
                <li key={index} className="border p-4 rounded shadow">
                  <p className="font-semibold">{review.userName || "Người dùng ẩn danh"}</p>
                  <p>⭐ {review.rating} / 5</p>
                  <p>{review.comment}</p>
                </li>
              ))}
            </ul>
          )}

          <DialogFooter>
            <Button variant="outline" onClick={() => setIsReviewModalOpen(false)}>
              Đóng
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Toaster position="top-right" />
    </DashboardLayout>
  )
}
