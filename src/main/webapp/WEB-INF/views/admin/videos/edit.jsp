<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Video - Video Portal Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="fas fa-video"></i> Video Portal Admin
            </a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/categories/list"><i class="fas fa-tags"></i> Categories</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/users/list"><i class="fas fa-users"></i> Users</a></li>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/admin/videos/list"><i class="fas fa-play-circle"></i> Videos</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-md-8 mx-auto">
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/videos/list">Videos</a></li>
                        <li class="breadcrumb-item active">Edit Video</li>
                    </ol>
                </nav>

                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0"><i class="fas fa-edit"></i> Edit Video</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${error != null}">
                            <div class="alert alert-danger" role="alert">
                                <i class="fas fa-exclamation-circle"></i> ${error}
                            </div>
                        </c:if>

                        <form method="post" action="${pageContext.request.contextPath}/admin/videos/edit">
                            <input type="hidden" name="id" value="${video.videoId}">
                            
                            <div class="mb-3">
                                <label for="title" class="form-label">Title <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" 
                                       value="${video.title}" required maxlength="200">
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description" 
                                          rows="4" placeholder="Enter video description...">${video.description}</textarea>
                            </div>

                            <div class="mb-3">
                                <label for="url" class="form-label">Video URL <span class="text-danger">*</span></label>
                                <input type="url" class="form-control" id="url" name="url" 
                                       value="${video.url}" required maxlength="255">
                            </div>

                            <div class="mb-3">
                                <label for="categoryId" class="form-label">Category <span class="text-danger">*</span></label>
                                <select class="form-select" id="categoryId" name="categoryId" required>
                                    <option value="">Select Category</option>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.categoryId}" ${video.category.categoryId == category.categoryId ? 'selected' : ''}>${category.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="uploaderId" class="form-label">Uploader <span class="text-danger">*</span></label>
                                <select class="form-select" id="uploaderId" name="uploaderId" required>
                                    <option value="">Select Uploader</option>
                                    <c:forEach var="user" items="${users}">
                                        <option value="${user.userId}" ${video.uploader.userId == user.userId ? 'selected' : ''}>${user.fullName} (${user.email})</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <small class="text-muted"><strong>Created:</strong> ${video.createdAt}</small>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/admin/videos/list" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i> Back to List
                                </a>
                                <div>
                                    <button type="reset" class="btn btn-outline-secondary me-2">
                                        <i class="fas fa-undo"></i> Reset
                                    </button>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-save"></i> Update Video
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
