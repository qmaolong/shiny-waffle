<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>无锡克维拉收银系统-首页</title>
    <link href="/asset/site/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <!-- Custom Theme files -->
    <link href="/asset/site/css/style.css" rel="stylesheet" type="text/css" media="all"/>
    <link href="/asset/site/css/style1.css" rel="stylesheet" type="text/css" media="all" />
    <!-- Custom Theme files -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="keywords" content="收银，收银系统，克维拉科技，无锡克维拉科技" />
    <script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
    <script src="/asset/site/js/jquery-1.11.0.min.js"></script>
    <link rel="stylesheet" href="/asset/site/css/flexslider.css" type="text/css" media="screen" />

</head>
<body>
<!--header strat here-->
<div class="banner agileits">
    <div class="header">
        <div class="container">
            <div class="header-main">
                <div class="logo">
                    <h1><span class="books"></span> <a href="/" style="font-family:'黑体';font-size:30px">克维拉收银系统</a></h1>
                </div>
                <div class="top-nav">
                    <span class="menu"> <img src="/asset/site/images/icon.png" alt=""></span>
                    <nav class="cl-effect-21" id="cl-effect-21">
                        <ul class="res">
                            <li><a href="/" class="active">首页</a></li>
                            <%--<li><a href="/asset/site/shortcodes.html">Short Codes</a></li>--%>
                            <li><a href="/asset/site/gallery.html">产品</a></li>
                            <li><a href="/asset/site/about.html">关于我们</a></li>
                            <li><a href="/asset/site/contact.html">联系我们</a></li>
                            <li><a href="/merchant/login" target="_blank">注册|登录</a></li>
                        </ul>
                    </nav>
                    <!-- script-for-menu -->
                    <script>
                        $( "span.menu" ).click(function() {
                            $( "ul.res" ).slideToggle( 300, function() {
                                // Animation complete.
                            });
                        });
                    </script>
                    <!-- /script-for-menu -->

                </div>
                <div class="clearfix"> </div>
            </div>
            <div class="banner-main">
                <section class="slider">
                    <div class="flexslider">
                        <ul class="slides">
                            <li>
                                <div class="banner-main">
                                    <h3>云收银系统</h3>
                                    <p>让你随时随地掌握最新收银状况，只要打开手机就可获得贵店各种报表信息，赚钱无忧</p>
                                    <div class="clearfix"> </div>
                                </div>
                            </li>
                            <li>
                                <div class="banner-main">
                                    <h3>化繁为简，高度定制</h3>
                                    <p>界面简单易用，一气呵成，简单培训即可上手，收银从未如此轻松</p>
                                    <div class="clearfix"> </div>
                                </div>
                            </li>
                            <li>
                                <div class="banner-main">
                                    <h3>数据云端保存，永远在线</h3>
                                    <p>数据云存储彻底彻底解决了因硬件故障导致的数据丢失</p>
                                    <div class="clearfix"> </div>
                                </div>
                            </li>
                            <li>
                                <div class="banner-main">
                                    <h3>全流程管控</h3>
                                    <p>收银，结账，订单流动，进度跟踪</p>
                                    <div class="clearfix"> </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="clearfix"> </div>
                </section>
            </div>
        </div>
    </div>
</div>
<!--header end here-->
<!-- FlexSlider -->
<script defer src="/asset/site/js/jquery.flexslider.js"></script>
<script type="text/javascript">
    $(function(){
    });
    $(window).load(function(){
        $('.flexslider').flexslider({
            animation: "slide",
            start: function(slider){
                $('body').removeClass('loading');
            }
        });
    });
</script>
<!-- FlexSlider -->
<!--banner end here-->
<!--educate logos start here-->
<div class="educate">
    <div class="container">
        <div class="education-main">
            <ul class="ch-grid">
                <div class="col-md-3 w3agile">
                    <li>
                        <div class="ch-item">
                            <div class="ch-info">
                                <div class="ch-info-front ch-img-1">
                                    <span class="glyphicon glyphicon-grain" aria-hidden="true"> </span>
                                    <h5>技术支持</h5>
                                </div>
                                <div class="ch-info-back">
                                    <h3>技术支持</h3>
                                    <p>系统永远的在线更新，给你无尽的惊喜</p>
                                </div>
                            </div>
                        </div>
                    </li>
                </div>
                <div class="col-md-3 w3agile">
                    <li>
                        <div class="ch-item">
                            <div class="ch-info">
                                <div class="ch-info-front ch-img-2">
                                    <span class="glyphicon glyphicon-education" aria-hidden="true"> </span>
                                    <h5>需求对接</h5>
                                </div>
                                <div class="ch-info-back">
                                    <h3>需求对接</h3>
                                    <p>你的信任，意见是给予我们最大的支持</p>
                                </div>
                            </div>
                        </div>
                    </li>
                </div>
                <div class="col-md-3 w3agile">
                    <li>
                        <div class="ch-item">
                            <div class="ch-info">
                                <div class="ch-info-front ch-img-3">
                                    <span class="glyphicon glyphicon-hourglass" aria-hidden="true"> </span>
                                    <h5>欢迎来访</h5>
                                </div>
                                <div class="ch-info-back">
                                    <h3>欢迎来访</h3>
                                    <p>随时欢迎到店参观，培训，听课</p>
                                </div>
                            </div>
                        </div>
                    </li>
                </div>
                <div class="col-md-3 w3agile">
                    <li>
                        <div class="ch-item">
                            <div class="ch-info">
                                <div class="ch-info-front ch-img-4">
                                    <span class="glyphicon glyphicon-eye-open" aria-hidden="true"> </span>
                                    <h5>售后保障</h5>
                                </div>
                                <div class="ch-info-back">
                                    <h3>售后保障</h3>
                                    <p>专业的售后和运行保障随时待命</p>
                                </div>
                            </div>
                        </div>
                    </li>
                </div>
                <div class="clearfix"> </div>
            </ul>
        </div>
    </div>
</div>
<!--educate logos end here-->
<!--pop-up-box-->
<script type="text/javascript" src="/asset/site/js/modernizr.custom.53451.js"></script>
<link href="/asset/site/css/popuo-box.css" rel="stylesheet" type="text/css" media="all"/>
<script src="/asset/site/js/jquery.magnific-popup.js" type="text/javascript"></script>
<!--pop-up-box-->
<script src="/asset/site/js/responsiveslides.min.js"></script>
<script>
    // You can also use "$(window).load(function() {"
    $(function () {
        $("#slider2").responsiveSlides({
            auto: true,
            pager: true,
            speed: 300,
            namespace: "callbacks",
        });
    });
</script>
<!--footer start here-->
<div class="footer w3ls">
    <div class="container">
        <div class="footer-main">
            <div class="footer-top">
                <div class="col-md-4 ftr-grid">
                    <h3>欢迎试用</h3>
                    <p>只要您点击注册按钮，注册您的店铺即可获得一个月的试用期限。更多精彩尽在其中</p>
                </div>
                <div class="col-md-4 ftr-grid">
                    <h3>联系方式</h3>
                    <div class="ftr-address">
                        <div class="local">
                            <span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span>
                        </div>
                        <div class="ftr-text">
                            <p>江苏省无锡市滨湖区隐秀路901号联创大厦507</p>
                        </div>
                        <div class="clearfix"> </div>
                    </div>
                    <div class="ftr-address">
                        <div class="local">
                            <span class="glyphicon glyphicon-phone" aria-hidden="true"></span>
                        </div>
                        <div class="ftr-text">
                            <p>0510-85860889</p>
                        </div>
                        <div class="clearfix"> </div>
                    </div>
                </div>
                <div class="col-md-4 ftr-grid">
                    <h3>留言咨询</h3>
                    <form action="#" method="post">
                        <input type="text" placeholder="邮件地址"  name="Enter Email" required=""/>
                        <input type="submit" value="">
                    </form>
                </div>
                <div class="clearfix"> </div>
            </div>
            <div class="footer-bottom">
                <div class="col-md-6 col-md-offset-3 copyrights">
                    <p>Copyright &copy; 2016 无锡克维拉科技有限公司 苏ICP备16044285号</p>
                </div>
                <div class="clearfix"> </div>
            </div>
        </div>
    </div>
</div>
<!--footer end here-->
</body>
</html>