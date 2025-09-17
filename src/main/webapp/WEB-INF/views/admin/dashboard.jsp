<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Dashboard" />
<c:set var="pageActive" value="dashboard" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Video Portal Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet">
    <style>
        .bg-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important; }
        .bg-success { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%) !important; }
        .bg-info { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%) !important; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="fas fa-video"></i> Video Portal Admin
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/admin/dashboard">
                            <i class="fas fa-tachometer-alt"></i> Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/categories/list">
                            <i class="fas fa-tags"></i> Categories
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/users/list">
                            <i class="fas fa-users"></i> Users
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/videos/list">
                            <i class="fas fa-play-circle"></i> Videos
                        </a>
                    </li>
                </ul>
                
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user"></i> Admin
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#"><i class="fas fa-cog"></i> Settings</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="#"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-12">
                <h1 class="h2 mb-4">
                    <i class="fas fa-tachometer-alt"></i> Dashboard
                </h1>
                
                <!-- Statistics Cards -->
                <div class="row">
                    <div class="col-md-4 mb-4">
                        <div class="card text-white bg-primary">
                            <div class="card-header">
                                <div class="row align-items-center">
                                    <div class="col">
                                        <i class="fas fa-tags fa-2x"></i>
                                    </div>
                                    <div class="col-auto">
                                        <div class="h1 mb-0">${categoryCount}</div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">Categories</h5>
                                <p class="card-text">Total video categories</p>
                                <a href="${pageContext.request.contextPath}/admin/categories/list" class="btn btn-light btn-sm">
                                    <i class="fas fa-arrow-right"></i> View All
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-4 mb-4">
                        <div class="card text-white bg-success">
                            <div class="card-header">
                                <div class="row align-items-center">
                                    <div class="col">
                                        <i class="fas fa-users fa-2x"></i>
                                    </div>
                                    <div class="col-auto">
                                        <div class="h1 mb-0">${userCount}</div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">Users</h5>
                                <p class="card-text">Total registered users</p>
                                <a href="${pageContext.request.contextPath}/admin/users/list" class="btn btn-light btn-sm">
                                    <i class="fas fa-arrow-right"></i> View All
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-4 mb-4">
                        <div class="card text-white bg-info">
                            <div class="card-header">
                                <div class="row align-items-center">
                                    <div class="col">
                                        <i class="fas fa-play-circle fa-2x"></i>
                                    </div>
                                    <div class="col-auto">
                                        <div class="h1 mb-0">${videoCount}</div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">Videos</h5>
                                <p class="card-text">Total uploaded videos</p>
                                <a href="${pageContext.request.contextPath}/admin/videos/list" class="btn btn-light btn-sm">
                                    <i class="fas fa-arrow-right"></i> View All
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Quick Actions -->
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0"><i class="fas fa-bolt"></i> Quick Actions</h5>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-4 mb-3">
                                        <a href="${pageContext.request.contextPath}/admin/categories/add" class="btn btn-outline-primary w-100">
                                            <i class="fas fa-plus"></i> Add New Category
                                        </a>
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <a href="${pageContext.request.contextPath}/admin/users/add" class="btn btn-outline-success w-100">
                                            <i class="fas fa-user-plus"></i> Add New User
                                        </a>
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <a href="${pageContext.request.contextPath}/admin/videos/add" class="btn btn-outline-info w-100">
                                            <i class="fas fa-video"></i> Add New Video
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
