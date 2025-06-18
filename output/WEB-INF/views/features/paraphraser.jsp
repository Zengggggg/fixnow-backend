<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fixnow</title>
    <link rel="icon" type="image/png" href="images/light-logo.png" />
    <link rel="stylesheet" href="css/dashboard.css" />
    <style>
        textarea::placeholder {
            font-size: 0.8rem; /* Adjust size as needed */
            color: #999;       /* Light gray color */
        }
    </style>
</head>

<body>
<!--  Body Wrapper -->
<div class="page-wrapper" id="main-wrapper" data-layout="vertical" data-navbarbg="skin6" data-sidebartype="full"
     data-sidebar-position="fixed" data-header-position="fixed" >

    <!-- App Topstrip -->
    <div class="app-topstrip bg-custom-blue py-3 px-4 w-100 d-lg-flex align-items-center justify-content-between">
        <div class="d-none d-sm-flex align-items-center justify-content-center gap-9 mb-3 mb-lg-0" >
            <a class="d-flex justify-content-center" href="/home">
                <img src="images/light-logo.png" width="50">
            </a>

            <div class="position-fixed top-2 end-0 z-3 d-none d-xl-flex align-items-center gap-2 border-start border-white ps-9">
                <!-- Templates link removed here -->
                <a target="_blank" href="#"
                   class="link-hover d-flex align-items-center gap-3 border-0 text-white lh-sm fs-4 me-4">
                    <iconify-icon class="fs-6" icon="solar:question-circle-linear"></iconify-icon>
                    Help
                </a>
                <a target="_blank" href="#"
                   class="link-hover d-flex align-items-center gap-3 border-0 text-white lh-sm fs-4 me-4">
                    <iconify-icon class="fs-6" icon="solar:case-round-linear"></iconify-icon>
                    Contact Us
                </a>
            </div>
        </div>

    </div>
    <!-- Sidebar Start -->
    <aside class="left-sidebar">
        <!-- Sidebar scroll-->
        <div>
            <div class="brand-logo d-flex align-items-center justify-content-between" style="border-right: #635bff;" >
                <a href="#" class="text-nowrap logo-img">
                    <div style="background-color: rgb(1, 1, 63); padding: 3px; border-radius: 50%; display: inline-block;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="white" viewBox="0 0 24 24">
                            <path d="M12 2a5 5 0 1 1 0 10 5 5 0 0 1 0-10zm0 12c-4.418 0-8 2.239-8 5v1h16v-1c0-2.761-3.582-5-8-5z"/>
                        </svg>
                    </div>


                </a>

                <!-- <div class="close-btn d-xl-none d-block sidebartoggler cursor-pointer" id="sidebarCollapse">
                    <i class="ti ti-x fs-8"></i>
                </div> -->
            </div>


            <!-- Sidebar navigation-->
            <nav class="sidebar-nav scroll-sidebar" data-simplebar="">
                <ul id="sidebarnav">
                    <li class="nav-small-cap d-flex align-items-center">
                        <iconify-icon icon="solar:menu-dots-linear" class="nav-small-cap-icon fs-4 me-2"></iconify-icon>
                        <div class="d-flex align-items-center">
                            <iconify-icon icon="material-symbols:home-outline" class="fs-6 me-2" style="color: #635bff;"></iconify-icon>
                            <span class="fs-12" style="color:#635bff;">Home</span>
                        </div>
                    </li>

                    <li class="sidebar-item">
                        <a class="sidebar-link btn-primary" href="/paraphraser">
                            <iconify-icon icon="tabler:repeat"></iconify-icon>
                            <span class="hide-menu">Paraphraser</span>
                        </a>
                    </li>

                    <li class="sidebar-item">
                        <a class="sidebar-link" href="/grammar_checker">
                            <iconify-icon icon="material-symbols:spellcheck"></iconify-icon>
                            <span class="hide-menu">Grammar Checker</span>
                        </a>
                    </li>
                    <li class="sidebar-item">
                        <a class="sidebar-link" href="/summarizer">
                            <iconify-icon icon="mdi:format-quote-close"></iconify-icon>
                            <span class="hide-menu">Summarizer</span>
                        </a>
                    </li>
                    <li class="sidebar-item">
                        <a class="sidebar-link" href="/translate">
                            <iconify-icon icon="mdi:translate"></iconify-icon>
                            <span class="hide-menu">Translate</span>
                        </a>
                    </li>
                    <li class="sidebar-item">
                        <a class="sidebar-link justify-content-between has-arrow" href="javascript:void(0)" aria-expanded="false">
                            <div class="d-flex align-items-center gap-3">
                  <span class="d-flex">
                    <iconify-icon icon="mdi:toolbox-outline"></iconify-icon>
                  </span>
                                <span class="hide-menu">Toolkits</span>
                            </div>
                        </a>
                        <ul aria-expanded="false" class="collapse first-level">
                            <li class="sidebar-item">
                                <a class="sidebar-link justify-content-between"
                                   href="#">
                                    <div class="d-flex align-items-center gap-3">
                      <span class="d-flex">
                        <span class="icon-small"></span>
                      </span>
                                        <span class="hide-menu">Email writer</span>
                                    </div>
                                </a>
                            </li>
                            <li class="sidebar-item">
                                <a class="sidebar-link justify-content-between"
                                   href="#">
                                    <div class="d-flex align-items-center gap-3">
                      <span class="d-flex">
                        <span class="icon-small"></span>
                      </span>
                                        <span class="hide-menu">CV generator</span>
                                    </div>
                                </a>
                            </li>
                            <li class="sidebar-item">
                                <a class="sidebar-link justify-content-between"
                                   href="#">
                                    <div class="d-flex align-items-center gap-3">
                      <span class="d-flex">
                        <span class="icon-small"></span>
                      </span>
                                        <span class="hide-menu">Contact Us</span>
                                    </div>
                                </a>
                            </li>
                            <li class="sidebar-item">
                                <a class="sidebar-link justify-content-between"
                                   href="#">
                                    <div class="d-flex align-items-center gap-3">
                      <span class="d-flex">
                        <span class="icon-small"></span>
                      </span>
                                        <span class="hide-menu">Pricing</span>
                                    </div>
                                </a>
                            </li>
                        </ul>
                    </li>

                    <li>
                        <span class="sidebar-divider lg"></span>
                    </li>

                </ul>
                <div
                        class="unlimited-access d-inline-flex align-items-center bg-primary-subtle position-relative p-3 rounded-3 gap-2">
                    <h6 class="fw-semibold fs-3 mb-0 text-black lh-sm">Fixnow Premium</h6>
                    <a href="#" class="btn btn-primary fs-2 fw-semibold lh-sm">Check</a>
                </div>

            </nav>
            <!-- End Sidebar navigation -->
        </div>
        <!-- End Sidebar scroll-->
    </aside>
    <!--  Sidebar End -->
    <!--  Main wrapper -->
    <div class="body-wrapper">
        <!--  Header Start -->
        <header class="app-header">
            <div class="d-flex align-items-center justify-content-between">
                <div class="d-flex align-items-center gap-3">
                    <div class="sidebar-toggler d-xl-none d-block cursor-pointer" id="sidebarCollapse">
                        <i class="ti ti-align-justified fs-8"></i>
                    </div>
                    <h4 class="position-absolute top-10 start-50 translate-middle m-0 fw-bold" style="position: relative; top: 30px;" >Paraphraser</h4>
                </div>
            </div>
        </header>
        <!--  Header End -->
        <div class="body-wrapper-inner">
            <div class="container-fluid mt-4">
                <div class="row">
                    <div class="mb-2">
                        <select id="languageSelector" style="background-color: white;" class="form-select w-auto">
                            <option value="en" selected>English (US)</option>
                            <option value="fr">French</option>
                            <option value="es">Spanish</option>
                            <option value="de">German</option>
                            <option value="vi">Vietnamese</option>
                        </select>
                    </div>
                    <!-- Left textarea -->
                    <div class="col-md-6 mb-3">
                        <textarea id="inputText" style="background: white" class="form-control" rows="10" placeholder="Enter or paste your text here..."></textarea>

                        <div class="d-flex justify-content-start mt-2">
                            <!-- Paste button -->
                            <button id="pasteBtn" class="btn btn-outline-primary" onclick="pasteText()">
                                <i class="bi bi-clipboard"></i> Paste Text
                            </button>

                            <!-- Word count badge (hidden by default) -->
                            <div id="wordCountBadge" class="btn btn-outline-secondary ms-2" style="display: none; pointer-events: none;">
                                0 / 125 words
                            </div>
                        </div>
                    </div>
                    <!-- Right textarea -->
                    <div class="col-md-6 mb-3">
                        <textarea id="outputText" style="background-color: white;" class="form-control" rows="10" placeholder="Paraphrased text will appear here..." readonly></textarea>
                        <div class="d-flex justify-content-end mt-2">
                            <button class="btn btn-primary" onclick="paraphraseText()">Paraphrase</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>
<script src="js/sidebarmenu.js"></script>
<script src="js/app.js"></script>
<!-- solar icons -->
<script src="https://cdn.jsdelivr.net/npm/iconify-icon@1.0.8/dist/iconify-icon.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    function paraphraseText() {
        const inputText = document.getElementById('inputText').value.trim();
        const wordCount = inputText.split(/\s+/).filter(w => w.length > 0).length;

        if (wordCount > 125) {
            Swal.fire({
                icon: 'warning',
                title: 'Reached word limit!',
                text: 'Vui lòng nâng cấp tài khoản Premium để sử dụng hơn 125 từ.',
                confirmButtonText: 'Nâng cấp ngay',
                confirmButtonColor: '#d33'
            });
            return;
        }

        // Gửi request nếu chưa vượt giới hạn
        fetch('/paraphrase', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ text: inputText })
        })
            .then(response => response.json())
            .then(data => {
                document.getElementById('outputText').value = data.paraphrasedText;
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Paraphrasing failed.');
            });
    }
</script>
<script>
    const inputTextArea = document.getElementById('inputText');
    const wordCountBadge = document.getElementById('wordCountBadge');
    const pasteBtn = document.getElementById('pasteBtn');

    inputTextArea.addEventListener('input', updateWordCount);

    function updateWordCount() {
        const text = inputTextArea.value.trim();
        const words = text.split(/\s+/).filter(w => w.length > 0);
        const count = words.length;

        // Toggle visibility based on input
        if (text.length > 0) {
            pasteBtn.style.display = 'none';
            wordCountBadge.style.display = 'inline-block';
        } else {
            pasteBtn.style.display = 'inline-block';
            wordCountBadge.style.display = 'none';
            document.getElementById('outputText').value = '';
        }

        // Update word count text
        wordCountBadge.textContent = count+` / 125 words`;

        // Change badge color if over limit
        if (count > 125) {
            wordCountBadge.classList.remove('btn-outline-secondary');
            wordCountBadge.classList.add('btn-danger');
        } else {
            wordCountBadge.classList.remove('btn-danger');
            wordCountBadge.classList.add('btn-outline-secondary');
        }
    }

    function pasteText() {
        navigator.clipboard.readText()
            .then(text => {
                inputTextArea.value = text;
                updateWordCount(); // trigger update immediately
            })
            .catch(err => {
                alert('Could not paste text: ' + err);
            });
    }
</script>

</body>

</html>