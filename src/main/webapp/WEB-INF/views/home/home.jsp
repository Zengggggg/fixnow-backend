<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>FixNow</title>
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="images/light-logo.png" />
    <!-- Bootstrap icons-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Core theme CSS (includes Bootstrap)-->
    <link href="css/home.css" rel="stylesheet" />
</head>
<body>
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <nav class="navbar navbar-expand-lg py-3">
                <div class="container">

                    <a class="navbar-brand me-4" href="#">
                        <img src="images/Logo.png" />
                        <b>Fixnow</b>
                    </a>

                    <!-- Left-aligned dropdowns -->
                    <div class="d-flex align-items-center">
                        <div class="dropdown me-3">
                            <a class="btn btn-link dropdown-toggle text-dark text-decoration-none" href="#" id="featuresDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                Features
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="featuresDropdown">
                                <li><a class="dropdown-item" href="#">Feature 1</a></li>
                                <li><a class="dropdown-item" href="#">Feature 2</a></li>
                            </ul>
                        </div>

                        <div class="dropdown me-4">
                            <a class="btn btn-link dropdown-toggle text-dark text-decoration-none" href="#" id="pricingDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                Pricing
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="pricingDropdown">
                                <li><a class="dropdown-item" href="#">Plan A</a></li>
                                <li><a class="dropdown-item" href="#">Plan B</a></li>
                            </ul>
                        </div>
                    </div>

                    <!-- Toggle for mobile view -->
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                        <span class="navbar-toggler-icon"></span>
                    </button>

                    <!-- Right-aligned nav items -->
                    <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                        <ul class="navbar-nav">
                            <li class="nav-item"><a class="nav-link me-4" href="#">Contact</a></li>
                            <li class="nav-item d-flex align-items-center">
                                <a class="btn btn-upgrade me-4" href="#" style="background-color: #A958FF; color: white; border-radius: 10px; padding: 6px 12px; text-decoration: none;">
                                    Upgrade Your Account
                                </a>
                                <a href="#" class="nav-link p-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                                        <path d="M13.468 12.37C12.758 11.226 11.44 10.5 8 10.5s-4.758.726-5.468 1.87A6.987 6.987 0 0 0 8 15a6.987 6.987 0 0 0 5.468-2.63z"/>
                                        <path fill-rule="evenodd" d="M8 9a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"/>
                                        <path fill-rule="evenodd" d="M8 1a7 7 0 1 0 0 14A7 7 0 0 0 8 1z"/>
                                    </svg>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <!-- Header-->
            <header class="py-3">
                <div class="container px-lg-5">
                    <div class="row g-3 align-items-stretch">
                        <!-- Left Box: Text -->
                        <div class="col-md-6 d-flex">
                            <div class="custom-auth-box bg-light">
                                <h1 class="headline">“Viết hay hơn theo phong cách của bạn”</h1>
                                <p class="description">Use AI power to enhance and improve your writing performance – while retaining your individuality.</p>
                                <div class="button-group">
                                    <a class="btn btn-primary me-2 signup-btn" href="#">Go to your workspace</a>
                                </div>
                            </div>
                        </div>

                        <!-- Right Box: Image -->
                        <div class="col-md-6">
                            <div class="bg-light p-4 h-100 rounded-3 d-flex justify-content-center align-items-center">
                                <img src="images/header.png" alt="Header Illustration" style="max-height: 500px; width: auto;" />
                            </div>
                        </div>
                    </div>
                </div>
            </header>
            <!-- Page Content-->
            <section class="pt-4">
                <div class="container px-lg-5">
                    <!-- Page Features-->
                    <div class="row gx-lg-5">
                        <div class="col-lg-6 col-xxl-4 mb-5">
                            <div class="card bg-light border-0 h-100">
                                <div class="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-4 mt-n4"><i class="bi bi-arrow-repeat"></i></div>
                                    <h2 class="fs-4 fw-bold">Paraphraser</h2>
                                    <p class="mb-0">Rewrite your sentences to improve clarity and originality.</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6 col-xxl-4 mb-5">
                            <div class="card bg-light border-0 h-100">
                                <div class="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-4 mt-n4"><i class="bi bi-journal-check"></i></div>
                                    <h2 class="fs-4 fw-bold">Grammar checker</h2>
                                    <p class="mb-0">Detect and correct grammar mistakes instantly.</p>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-6 col-xxl-4 mb-5">
                            <div class="card bg-light border-0 h-100">
                                <div class="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-4 mt-n4"><i class="bi bi-file-earmark-text"></i></div>
                                    <h2 class="fs-4 fw-bold">Summarization</h2>
                                    <p class="mb-0">Condense long texts into concise summaries.</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6 col-xxl-4 mb-5">
                            <div class="card bg-light border-0 h-100">
                                <div class="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-4 mt-n4"><i class="bi bi-briefcase"></i></div>
                                    <h2 class="fs-4 fw-bold">Toolkits</h2>
                                    <p class="mb-0">Start Bootstrap has been the leader in free Bootstrap templates since 2013!</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <!-- Footer-->
            <footer class="py-5 bg-dark">
                <div class="container"><p class="m-0 text-center text-white">Copyright &copy; Your Website 2023</p></div>
            </footer>

        </c:when>
        <c:otherwise>
            <nav class="navbar navbar-expand-lg py-3">
                <div class="container">

                    <a class="navbar-brand me-4" href="#">
                        <img src="images/Logo.png" />
                        <b>Fixnow</b>
                    </a>

                    <!-- Left-aligned dropdowns -->
                    <div class="d-flex align-items-center">
                        <div class="dropdown me-3">
                            <a class="btn btn-link dropdown-toggle text-dark text-decoration-none" href="#" id="featuresDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                Features
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="featuresDropdown">
                                <li><a class="dropdown-item" href="#">Feature 1</a></li>
                                <li><a class="dropdown-item" href="#">Feature 2</a></li>
                            </ul>
                        </div>

                        <div class="dropdown me-4">
                            <a class="btn btn-link dropdown-toggle text-dark text-decoration-none" href="#" id="pricingDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                Pricing
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="pricingDropdown">
                                <li><a class="dropdown-item" href="#">Plan A</a></li>
                                <li><a class="dropdown-item" href="#">Plan B</a></li>
                            </ul>
                        </div>
                    </div>

                    <!-- Toggle for mobile view -->
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                        <span class="navbar-toggler-icon"></span>
                    </button>

                    <!-- Right-aligned nav items -->
                    <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                        <ul class="navbar-nav">
                            <li class="nav-item"><a class="nav-link me-4" href="#">Contact</a></li>
                            <li class="nav-item">
                                <a class="btn btn-login me-2" href="/login">Login</a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-signup" href="/register">Sign Up</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <!-- Header-->
            <header class="py-3">
                <div class="container px-lg-5">
                    <div class="row g-3 align-items-stretch">
                        <!-- Left Box: Text -->
                        <div class="col-md-6 d-flex">
                            <div class="custom-auth-box bg-light">
                                <h1 class="headline">“Viết hay hơn theo phong cách của bạn”</h1>
                                <p class="description">Use AI power to enhance and improve your writing performance – while retaining your individuality.</p>
                                <div class="button-group">
                                    <a class="btn btn-primary me-2 signup-btn" href="/login">Sign up It’s free →</a>
                                    <button class="btn btn-outline-secondary google-btn">
                                        <img src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg" alt="Google" width="20" class="me-2">
                                        Sign up with google
                                    </button>
                                </div>
                                <p class="terms-text mt-3">By signing up, you agree to the <a href="#">Terms and Conditions</a> and <a href="#">Privacy Policy</a>.</p>
                            </div>
                        </div>

                        <!-- Right Box: Image -->
                        <div class="col-md-6">
                            <div class="bg-light p-4 h-100 rounded-3 d-flex justify-content-center align-items-center">
                                <img src="images/header.png" alt="Header Illustration" style="max-height: 500px; width: auto;" />
                            </div>
                        </div>
                    </div>
                </div>
            </header>
            <!-- Page Content-->
            <section class="pt-4">
                <div class="container px-lg-5">
                    <!-- Page Features-->
                    <div class="row gx-lg-5">
                        <div class="col-lg-6 col-xxl-4 mb-5">
                            <div class="card bg-light border-0 h-100">
                                <div class="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-4 mt-n4"><i class="bi bi-arrow-repeat"></i></div>
                                    <h2 class="fs-4 fw-bold">Paraphraser</h2>
                                    <p class="mb-0">Rewrite your sentences to improve clarity and originality.</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6 col-xxl-4 mb-5">
                            <div class="card bg-light border-0 h-100">
                                <div class="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-4 mt-n4"><i class="bi bi-journal-check"></i></div>
                                    <h2 class="fs-4 fw-bold">Grammar checker</h2>
                                    <p class="mb-0">Detect and correct grammar mistakes instantly.</p>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-6 col-xxl-4 mb-5">
                            <div class="card bg-light border-0 h-100">
                                <div class="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-4 mt-n4"><i class="bi bi-file-earmark-text"></i></div>
                                    <h2 class="fs-4 fw-bold">Summarization</h2>
                                    <p class="mb-0">Condense long texts into concise summaries.</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6 col-xxl-4 mb-5">
                            <div class="card bg-light border-0 h-100">
                                <div class="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-4 mt-n4"><i class="bi bi-briefcase"></i></div>
                                    <h2 class="fs-4 fw-bold">Toolkits</h2>
                                    <p class="mb-0">Start Bootstrap has been the leader in free Bootstrap templates since 2013!</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <!-- Footer-->
            <footer class="py-5 bg-dark">
                <div class="container"><p class="m-0 text-center text-white">Copyright &copy; Your Website 2023</p></div>
            </footer>
        </c:otherwise>
    </c:choose>
<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Core theme JS-->
<script src="js/scripts.js"></script>
</body>
</html>



