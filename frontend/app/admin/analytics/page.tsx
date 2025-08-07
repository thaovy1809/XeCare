"use client"

import type React from "react"

import { DashboardLayout } from "@/components/dashboard-layout"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { useState, useEffect, useMemo } from "react"
import {
  TrendingUp,
  TrendingDown,
  Users,
  Calendar,
  Star,
  DollarSign,
  Activity,
  Target,
  Download,
  RefreshCw,
  MapPin,
  Clock,
  AlertCircle,
  CheckCircle,
  BarChart3,
  PieChart,
  LineChart,
  Filter,
  Search,
  ArrowUpDown,
  ArrowUp,
  ArrowDown,
} from "lucide-react"

interface MetricCard {
  title: string
  value: string
  change: number
  changeType: "increase" | "decrease"
  icon: React.ReactNode
  color: string
}

interface ChartData {
  name: string
  value: number
  percentage?: number
}

interface RegionalData {
  region: string
  garages: number
  bookings: number
  revenue: string
  revenueValue: number
  growth: number
}

interface TopGarage {
  name: string
  location: string
  bookings: number
  rating: number
  revenue: string
  revenueValue: number
  completion: number
  responseTime: string
  responseTimeValue: number
}

type SortField = "name" | "bookings" | "rating" | "revenue" | "completion" | "responseTime"
type SortDirection = "asc" | "desc"

export default function AdminAnalyticsPage() {
  const [timeRange, setTimeRange] = useState("30d")
  const [isLoading, setIsLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState("")
  const [selectedRegion, setSelectedRegion] = useState("all")
  const [sortField, setSortField] = useState<SortField>("bookings")
  const [sortDirection, setSortDirection] = useState<SortDirection>("desc")
  const [lastUpdated, setLastUpdated] = useState(new Date())
  const [metrics, setMetrics] = useState<MetricsData | null>(null);
  const [serviceDistribution, setServiceDistribution] = useState<ChartData[]>([]);


  interface MetricsData {
    totalRevenue: number;
    appointments: number;
    activeGarages: number;
    averageRating: number;
    completionRate: number;
  }

  type BookingStatusItem = {
    status: string;
    count: number;
    percentage: number;
    color: string;
  };

  const [bookingStatus, setBookingStatus] = useState<BookingStatusItem[]>([]);

  type ReveData = {
    name: string;
    value: number;
  };

  const [revenueData, setRevenueData] = useState<ReveData[]>([]);

  useEffect(() => {
    const interval = setInterval(() => {
      setLastUpdated(new Date())
    }, 30000)
    return () => clearInterval(interval)
  }, [])

  const handleRefresh = async () => {
    setIsLoading(true)
    await new Promise((resolve) => setTimeout(resolve, 1000))
    setLastUpdated(new Date())
    setIsLoading(false)
  }

  const getKeyMetrics = (data: MetricsData | null): MetricCard[] => {
    if (!data) return [];

    return [
      {
        title: "Tổng doanh thu",
        value: `${(data.totalRevenue / 1_000_000).toFixed(1)}M VNĐ`,
        change: 12.5,
        changeType: "increase",
        icon: <DollarSign className="h-5 w-5" />,
        color: "text-green-600",
      },
      {
        title: "Lượt đặt lịch",
        value: `${data.appointments.toLocaleString()}`,
        change: 8.2,
        changeType: "increase",
        icon: <Calendar className="h-5 w-5" />,
        color: "text-blue-600",
      },
      {
        title: "Garage hoạt động",
        value: `${data.activeGarages.toLocaleString()}`,
        change: 5.8,
        changeType: "increase",
        icon: <MapPin className="h-5 w-5" />,
        color: "text-cyan-600",
      },
      {
        title: "Đánh giá trung bình",
        value: `${(data.averageRating ?? 0).toFixed(1)}/5`,
        change: 3.2,
        changeType: "increase",
        icon: <Star className="h-5 w-5" />,
        color: "text-yellow-600",
      }
      ,
    ];
  };

  const keyMetrics = getKeyMetrics(metrics);

  useEffect(() => {
    const fetchRevenueData = async () => {
      const res = await fetch(`http://localhost:8080/api/v1/revenue?year=2025`);
      const data = await res.json();
      setRevenueData(data);
    };

    fetchRevenueData();

    const fetchBookingStatus = async () => {
      const res = await fetch("http://localhost:8080/api/v1/booking-status");
      const data = await res.json();
      setBookingStatus(data);
    };

    fetchBookingStatus();

    const fetchServiceDistribution = async () => {
      const res = await fetch("http://localhost:8080/api/v1/service-distribution");
      const data = await res.json();
      setServiceDistribution(data);
    };

    fetchServiceDistribution();

  }, []);

  useEffect(() => {
    const fetchMetrics = async () => {
      const res = await fetch(`http://localhost:8080/api/v1/metrics?range=${timeRange}`);
      const data = await res.json();
      setMetrics(data);
    };

    if (timeRange) {
      fetchMetrics();
    }
  }, [timeRange]);




  return (
    <DashboardLayout
      allowedRoles={["admin"]}
      title="Phân tích & Thống kê"
      description="Báo cáo chi tiết về hoạt động của hệ thống"
    >
      {/* Header Controls */}
      <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center gap-4 mb-6">
        <div className="flex items-center space-x-4">
          <h2 className="text-2xl font-bold text-slate-900">Phân tích & Thống kê</h2>
          <Badge variant="outline" className="text-green-600 border-green-200">
            <div className="w-2 h-2 bg-green-500 rounded-full mr-2 animate-pulse"></div>
            Live Data
          </Badge>
          <span className="text-xs text-slate-500">Cập nhật: {lastUpdated.toLocaleTimeString("vi-VN")}</span>
        </div>

        <div className="flex flex-wrap items-center gap-3">
          {/* Time Range */}
          <Select value={timeRange} onValueChange={setTimeRange}>
            <SelectTrigger className="w-[140px]">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="7d">7 ngày qua</SelectItem>
              <SelectItem value="30d">30 ngày qua</SelectItem>
              <SelectItem value="90d">3 tháng qua</SelectItem>
              <SelectItem value="1y">1 năm qua</SelectItem>
            </SelectContent>
          </Select>

          {/* Refresh Button */}
          <Button variant="outline" size="sm" onClick={handleRefresh} disabled={isLoading}>
            <RefreshCw className={`h-4 w-4 mr-2 ${isLoading ? "animate-spin" : ""}`} />
            Làm mới
          </Button>
        </div>
      </div>

      {/* Key Metrics Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {keyMetrics.map((metric, index) => (
          <Card key={index} className="border-slate-200 hover:shadow-lg transition-shadow">
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div className="space-y-2">
                  <p className="text-sm font-medium text-slate-600">{metric.title}</p>
                  <p className={`text-2xl font-bold ${metric.color}`}>{metric.value}</p>
                  <div className="flex items-center space-x-1">
                    {metric.changeType === "increase" ? (
                      <TrendingUp className="h-3 w-3 text-green-600" />
                    ) : (
                      <TrendingDown className="h-3 w-3 text-red-600" />
                    )}
                    <span
                      className={`text-xs font-medium ${metric.changeType === "increase" ? "text-green-600" : "text-red-600"
                        }`}
                    >
                      {metric.changeType === "increase" ? "+" : ""}
                      {metric.change}%
                    </span>
                    <span className="text-xs text-slate-500">vs tháng trước</span>
                  </div>
                </div>
                <div className={`p-3 rounded-full bg-slate-50 ${metric.color}`}>{metric.icon}</div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Main Analytics Tabs */}
      <Tabs defaultValue="overview" className="space-y-6">
        <TabsList className="grid w-full grid-cols-5">
          <TabsTrigger value="overview" className="flex items-center space-x-2">
            <BarChart3 className="h-4 w-4" />
            <span>Tổng quan</span>
          </TabsTrigger>
          <TabsTrigger value="services" className="flex items-center space-x-2">
            <PieChart className="h-4 w-4" />
            <span>Dịch vụ</span>
          </TabsTrigger>
        </TabsList>

        {/* Overview Tab */}
        <TabsContent value="overview" className="space-y-6">
          <div className="grid lg:grid-cols-2 gap-6">
            {/* Revenue Trend Chart */}
            <Card className="border-slate-200">
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <LineChart className="h-5 w-5 text-blue-600" />
                  <span>Xu hướng doanh thu</span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div className="flex justify-between text-sm text-slate-600 mb-4">
                    <span>Doanh thu theo tháng (VNĐ)</span>
                  </div>
                  {revenueData.map((data, index) => (
                    <div key={index} className="flex items-center justify-between">
                      <span className="text-sm font-medium text-slate-700 w-8">{data.name}</span>
                      <div className="flex-1 mx-4">
                        <div className="w-full bg-slate-200 rounded-full h-2">
                          <div
                            className="bg-gradient-to-r from-blue-500 to-cyan-500 h-2 rounded-full transition-all duration-500"
                            style={{ width: `${(data.value / Math.max(...revenueData.map((d) => d.value))) * 100}%` }}
                          ></div>
                        </div>
                      </div>
                      <span className="text-sm text-slate-600 w-20 text-right">
                        {(data.value / 100000).toFixed(0)}K
                      </span>

                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>

            {/* Booking Status Distribution */}
            <Card className="border-slate-200">
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <PieChart className="h-5 w-5 text-green-600" />
                  <span>Phân bố trạng thái đặt lịch</span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {bookingStatus.map((status, index) => (
                    <div key={index} className="space-y-2">
                      <div className="flex justify-between items-center">
                        <div className="flex items-center space-x-3">
                          <div className={`w-3 h-3 rounded-full ${status.color}`}></div>
                          <span className="text-sm font-medium text-slate-700">{status.status}</span>
                        </div>
                        <div className="text-right">
                          <span className="text-sm font-semibold text-slate-900">{status.count.toLocaleString()}</span>
                          <span className="text-xs text-slate-500 ml-2">({status.percentage}%)</span>
                        </div>
                      </div>
                      <div className="w-full bg-slate-200 rounded-full h-2">
                        <div
                          className={`${status.color} h-2 rounded-full transition-all duration-500`}
                          style={{ width: `${status.percentage}%` }}
                        ></div>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        {/* Services Tab */}
        <TabsContent value="services" className="space-y-6">
          <Card className="border-slate-200">
            <CardHeader>
              <CardTitle className="flex items-center space-x-2">
                <Activity className="h-5 w-5 text-purple-600" />
                <span>Thống kê dịch vụ chi tiết</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid md:grid-cols-2 gap-8">
                {/* Service Distribution Chart */}
                <div className="space-y-4">
                  <h4 className="font-semibold text-slate-900">Phân bố dịch vụ</h4>
                  {serviceDistribution.map((service, index) => (
                    <div key={index} className="space-y-2">
                      <div className="flex justify-between items-center">
                        <span className="text-sm font-medium text-slate-700">{service.name}</span>
                        <div className="text-right">
                          <span className="text-sm font-semibold text-slate-900">{service.value}</span>
                          <span className="text-xs text-slate-500 ml-2">({service.percentage}%)</span>
                        </div>
                      </div>
                      <div className="w-full bg-slate-200 rounded-full h-3">
                        <div
                          className="bg-gradient-to-r from-purple-500 to-pink-500 h-3 rounded-full transition-all duration-700"
                          style={{ width: `${service.percentage}%` }}
                        ></div>
                      </div>
                    </div>
                  ))}
                </div>

                {/* Service Performance Metrics */}
                <div className="space-y-4">
                  <h4 className="font-semibold text-slate-900">Hiệu suất dịch vụ</h4>
                  <div className="space-y-3">
                    {serviceDistribution.map((service, index) => (
                      <div key={index} className="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                        <div>
                          <p className="font-medium text-slate-900">{service.name}</p>
                          <p className="text-sm text-slate-600">{service.value} lượt đặt</p>
                        </div>
                        <div className="text-right">
                          <div className="flex items-center space-x-2">
                            <Star className="h-4 w-4 text-yellow-400 fill-current" />
                            <span className="text-sm font-medium">4.{Math.floor(Math.random() * 9) + 1}</span>
                          </div>
                          <p className="text-xs text-green-600">+{Math.floor(Math.random() * 20) + 5}% tháng này</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </DashboardLayout>
  )
}
