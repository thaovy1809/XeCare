"use client"
import { Toaster } from "react-hot-toast"
import toast from "react-hot-toast"
import { useEffect, useState } from "react"
import { DashboardLayout } from "@/components/dashboard-layout"
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
  Search,
  MoreHorizontal,
  Eye,
  Lock,
  Unlock,
  Trash2,
  UserPlus,
  Download,
} from "lucide-react"

export default function AdminUsersPage() {
  const [users, setUsers] = useState<any[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [searchTerm, setSearchTerm] = useState("")
  const [roleFilter, setRoleFilter] = useState("all")
  const [statusFilter, setStatusFilter] = useState("all")
  const [isModalOpen, setIsModalOpen] = useState(false)


  const [isEditModalOpen, setIsEditModalOpen] = useState(false)
  const [editFormData, setEditFormData] = useState({
    id: "",
    name: "",
    email: "",
    phone: "",
    address: "",
    password: "",
  })
  const [editFormLoading, setEditFormLoading] = useState(false)
  const [editFormError, setEditFormError] = useState<string | null>(null)

  const openEditModal = (user: any) => {
    setEditFormData({
      id: user.id || 0,
      name: user.name || "",
      email: user.email || "",
      phone: user.phone || "",
      address: user.address || "",
      password: "",
    })
    setEditFormError(null)
    setIsEditModalOpen(true)
  }

  const handleEditInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setEditFormData(prev => ({ ...prev, [name]: value }))
  }
  const handleEditSubmit = async () => {
    setEditFormLoading(true)
    setEditFormError(null)

    if (!editFormData.name || !editFormData.email) {
      setEditFormError("Tên và Email là bắt buộc")
      setEditFormLoading(false)
      return
    }

    try {
      const res = await fetch(`http://localhost:8080/api/v1/users/${editFormData.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(editFormData),
      })
      if (!res.ok) throw new Error("Cập nhật thất bại")

      toast.success("Cập nhật người dùng thành công 🎉")
      fetchUsers()
      setIsEditModalOpen(false)
    } catch (error: any) {
      setEditFormError(error.message || "Lỗi không xác định")
    } finally {
      setEditFormLoading(false)
    }
  }


  // Form data
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    address: "",
    password: "",
  })

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleSubmit = async () => {
    setFormLoading(true)
    setFormError(null)
    try {
      const res = await fetch("http://localhost:8080/api/v1/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      })

      if (!res.ok) {
        const errData = await res.json()
        throw new Error(errData.message || "Failed to create user")
      }

      const newUser = await res.json()

      fetchUsers();
      toast.success("Thêm thành công 🎉")

      setIsModalOpen(false)
      setFormData({
        name: "",
        email: "",
        phone: "",
        address: "",
        password: "",
      })
    } catch (err: any) {
      setFormError(err.message || "Lỗi không xác định")
    } finally {
      setFormLoading(false)
    }
  }
  const fetchUsers = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/v1/users")
      if (!res.ok) throw new Error("Failed to fetch users")
      const data = await res.json()
      setUsers(data)
    } catch (err: any) {
      setError(err.message || "Lỗi không xác định")
    } finally {
      setLoading(false)
    }
  }


  const [formLoading, setFormLoading] = useState(false)
  const [formError, setFormError] = useState<string | null>(null)
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/v1/users")
        if (!res.ok) throw new Error("Failed to fetch users")
        const data = await res.json()
        setUsers(data)
      } catch (err: any) {
        setError(err.message || "Lỗi không xác định")
      } finally {
        setLoading(false)
      }
    }

    fetchUsers()
  }, [])

  const filteredUsers = users.filter((user) => {
    const matchesSearch =
      user.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.email?.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesRole = roleFilter === "all" || user.role === roleFilter
    const matchesStatus =
      statusFilter === "all" || user.status === statusFilter

    return matchesSearch && matchesRole && matchesStatus
  })

  const handleToggleStatus = async (userId: number) => {
    try {
      const res = await fetch(`http://localhost:8080/api/v1/users/${userId}/toggle-status`, {
        method: "PUT",
      });
      if (!res.ok) throw new Error("Không thể thay đổi trạng thái tài khoản");

      const updatedUser = await res.json();
      toast.success("Thay đổi trạng thái thành công 🎉")

      setUsers((prev) =>
        prev.map((u) => (u.id === userId ? updatedUser : u))
      );
    } catch (err: any) {
      console.error("Toggle status failed", err.message);
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "active":
        return <Badge className="bg-green-100 text-green-700">Hoạt động</Badge>
      case "locked":
        return <Badge className="bg-red-100 text-red-700">Bị khóa</Badge>
      case "pending":
        return <Badge className="bg-yellow-100 text-yellow-700">Chờ duyệt</Badge>
      default:
        return <Badge className="bg-gray-100 text-gray-700">Không xác định</Badge>
    }
  }

  const getRoleBadge = (role: string) => {
    switch (role) {
      case "admin":
        return <Badge className="bg-red-100 text-red-700">Admin</Badge>
      case "garage":
        return <Badge className="bg-blue-100 text-blue-700">Garage</Badge>
      case "user":
        return <Badge className="bg-green-100 text-green-700">User</Badge>
      default:
        return <Badge className="bg-gray-100 text-gray-700">Khác</Badge>
    }
  }

  const stats = {
    total: users.length,
    active: users.filter((u) => u.status === 1).length,
    locked: users.filter((u) => u.status === 0).length,
    pending: users.filter((u) => u.status === "pending").length,
  }

  if (loading) return <div className="p-6 text-center">Đang tải...</div>
  if (error) return <div className="p-6 text-center text-red-500">Lỗi: {error}</div>

  return (
    <DashboardLayout
      allowedRoles={["admin"]}
      title="Quản lý người dùng"
      description="Xem và quản lý tất cả người dùng trong hệ thống"
    >
      {/* Stats */}
      <div className="grid md:grid-cols-4 gap-6 mb-6">
        <Card><CardContent className="p-6 text-center"><p className="text-2xl font-bold text-blue-600">{stats.total}</p><p className="text-sm text-slate-600">Tổng người dùng</p></CardContent></Card>
        <Card><CardContent className="p-6 text-center"><p className="text-2xl font-bold text-green-600">{stats.active}</p><p className="text-sm text-slate-600">Đang hoạt động</p></CardContent></Card>
        <Card><CardContent className="p-6 text-center"><p className="text-2xl font-bold text-yellow-600">{stats.pending}</p><p className="text-sm text-slate-600">Chờ duyệt</p></CardContent></Card>
        <Card><CardContent className="p-6 text-center"><p className="text-2xl font-bold text-red-600">{stats.locked}</p><p className="text-sm text-slate-600">Bị khóa</p></CardContent></Card>
      </div>

      {/* Filters + Actions */}
      <Card className="border-blue-100 mb-6">
        <CardHeader>
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
            <CardTitle>Danh sách người dùng</CardTitle>
            <div className="flex gap-2">
              <Button
                className="bg-gradient-to-r from-blue-600 to-cyan-600"
                onClick={() => setIsModalOpen(true)}
              >
                <UserPlus className="h-4 w-4 mr-2" />
                Thêm người dùng
              </Button>
              <Button variant="outline" className="border-blue-200 text-blue-600">
                <Download className="h-4 w-4 mr-2" />
                Xuất Excel
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="flex flex-col sm:flex-row gap-4 mb-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-slate-400" />
              <Input
                placeholder="Tìm kiếm theo tên hoặc email..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={roleFilter} onValueChange={setRoleFilter}>
              <SelectTrigger className="w-full sm:w-[180px]">
                <SelectValue placeholder="Lọc theo vai trò" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tất cả vai trò</SelectItem>
                <SelectItem value="user">User</SelectItem>
                <SelectItem value="garage">Garage</SelectItem>
                <SelectItem value="admin">Admin</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* Users Table */}
          <div className="border rounded-lg">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Người dùng</TableHead>
                  <TableHead>Vai trò</TableHead>
                  <TableHead>Ngày tạo</TableHead>
                  <TableHead>Trạng thái</TableHead>
                  <TableHead className="text-right">Thao tác</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredUsers.map((user) => (
                  <TableRow key={user.id}>
                    <TableCell>
                      <div>
                        <p className="font-medium text-slate-900">{user.name}</p>
                        <p className="text-sm text-slate-500">{user.email}</p>
                        <p className="text-sm text-slate-500">{user.phone}</p>
                      </div>
                    </TableCell>

                    <TableCell>{getRoleBadge(user.role)}</TableCell>
                    <TableCell className="text-sm text-slate-600">{user.createdAt?.split("T")[0]}</TableCell>

                    <TableCell>{getStatusBadge(user.status === 1 ? "active" : "locked")}</TableCell>

                    <TableCell className="text-right">
                      <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                          <Button variant="ghost" size="sm">
                            <MoreHorizontal className="h-4 w-4" />
                          </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                          <DropdownMenuItem onClick={() => openEditModal(user)}>
                            <Eye className="h-4 w-4 mr-2" />
                            Xem chi tiết
                          </DropdownMenuItem>

                          <DropdownMenuItem onClick={() => handleToggleStatus(user.id)}>
                            {user.status === 1 ? (
                              <>
                                <Lock className="h-4 w-4 mr-2" />
                                Khóa tài khoản
                              </>
                            ) : (
                              <>
                                <Unlock className="h-4 w-4 mr-2" />
                                Kích hoạt tài khoản
                              </>
                            )}
                          </DropdownMenuItem>

                        </DropdownMenuContent>
                      </DropdownMenu>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          {filteredUsers.length === 0 && (
            <div className="text-center py-8 text-slate-500">Không tìm thấy người dùng nào</div>
          )}
        </CardContent>
      </Card>

      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogTrigger asChild>
        </DialogTrigger>
        <DialogContent className="sm:max-w-lg">
          <DialogHeader>
            <DialogTitle>Thêm người dùng mới</DialogTitle>
            <DialogDescription>Nhập thông tin người dùng để tạo tài khoản mới.</DialogDescription>
          </DialogHeader>

          <div className="grid gap-4 py-4">
            <Input
              name="name"
              placeholder="Tên người dùng"
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
              type="password"
              name="password"
              placeholder="Mật khẩu"
              value={formData.password}
              onChange={handleInputChange}
              required
            />
            {formError && <p className="text-red-600 text-sm">{formError}</p>}
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setIsModalOpen(false)} disabled={formLoading}>
              Hủy
            </Button>
            <Button onClick={handleSubmit} disabled={formLoading}>
              {formLoading ? "Đang tạo..." : "Tạo người dùng"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
      <Dialog open={isEditModalOpen} onOpenChange={setIsEditModalOpen}>
        <DialogContent className="sm:max-w-lg">
          <DialogHeader>
            <DialogTitle>Sửa thông tin người dùng</DialogTitle>
            <DialogDescription>Chỉnh sửa thông tin người dùng hiện tại.</DialogDescription>
          </DialogHeader>

          <div className="grid gap-4 py-4">
            <Input
              name="name"
              placeholder="Tên người dùng"
              value={editFormData.name}
              onChange={handleEditInputChange}
              required
            />
            <Input
              type="email"
              name="email"
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
              type="password"
              name="password"
              placeholder="Mật khẩu (để trống nếu không đổi)"
              value={editFormData.password}
              onChange={handleEditInputChange}
            />
            {editFormError && <p className="text-red-600 text-sm">{editFormError}</p>}
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setIsEditModalOpen(false)} disabled={editFormLoading}>
              Hủy
            </Button>
            <Button onClick={handleEditSubmit} disabled={editFormLoading}>
              {editFormLoading ? "Đang lưu..." : "Lưu thay đổi"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
      <Toaster position="top-right" />
    </DashboardLayout>
  )
}
