;(function(window) {

  var svgSprite = '<svg>' +
    '' +
    '<symbol id="icon-gouwuche" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M246.4 912a2.1 2.1 0 1 0 134.4 0 2.1 2.1 0 1 0-134.4 0Z"  ></path>' +
    '' +
    '<path d="M716.8 912a2.1 2.1 0 1 0 134.4 0 2.1 2.1 0 1 0-134.4 0Z"  ></path>' +
    '' +
    '<path d="M905.6 764.8l-537.6 0c-28.8 0-57.6-25.6-64-54.4l-96-566.4c-9.6-54.4-60.8-96-115.2-96l-22.4 0c-12.8 0-25.6 12.8-25.6 25.6 0 12.8 12.8 25.6 25.6 25.6l22.4 0c28.8 0 57.6 25.6 64 54.4l96 566.4c9.6 54.4 60.8 96 115.2 96l537.6 0c12.8 0 25.6-12.8 25.6-25.6C931.2 777.6 921.6 764.8 905.6 764.8z"  ></path>' +
    '' +
    '<path d="M880 179.2l-572.8 0c-12.8 0-25.6 12.8-25.6 25.6 0 12.8 12.8 25.6 25.6 25.6l572.8 0c25.6 0 38.4 16 32 41.6l-70.4 281.6c-6.4 32-41.6 57.6-73.6 60.8l-396.8 28.8c-12.8 0-25.6 12.8-22.4 28.8 0 12.8 12.8 25.6 28.8 22.4l396.8-28.8c54.4-3.2 105.6-48 118.4-99.2l70.4-281.6C976 230.4 937.6 179.2 880 179.2z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-erweima" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M384 64l-249.6 0c-51.2 0-89.6 41.6-89.6 89.6l0 227.2c0 51.2 41.6 89.6 89.6 89.6l249.6 0c51.2 0 89.6-41.6 89.6-89.6l0-227.2C473.6 105.6 435.2 64 384 64zM428.8 380.8c0 25.6-19.2 44.8-44.8 44.8l-249.6 0c-25.6 0-44.8-19.2-44.8-44.8l0-227.2c0-25.6 19.2-44.8 44.8-44.8l249.6 0c25.6 0 44.8 19.2 44.8 44.8L428.8 380.8z"  ></path>' +
    '' +
    '<path d="M192 192l134.4 0 0 134.4-134.4 0 0-134.4Z"  ></path>' +
    '' +
    '<path d="M377.6 544l-243.2 0c-48 0-86.4 38.4-86.4 89.6l0 220.8c0 48 38.4 89.6 86.4 89.6l243.2 0c48 0 86.4-38.4 86.4-89.6l0-220.8C467.2 582.4 425.6 544 377.6 544zM422.4 851.2c0 25.6-19.2 44.8-44.8 44.8l-243.2 0c-25.6 0-44.8-19.2-44.8-44.8l0-220.8c0-25.6 19.2-44.8 44.8-44.8l243.2 0c25.6 0 44.8 19.2 44.8 44.8L422.4 851.2z"  ></path>' +
    '' +
    '<path d="M192 668.8l131.2 0 0 131.2-131.2 0 0-131.2Z"  ></path>' +
    '' +
    '<path d="M633.6 470.4l249.6 0c51.2 0 89.6-41.6 89.6-89.6l0-227.2c0-51.2-41.6-89.6-89.6-89.6l-249.6 0c-51.2 0-89.6 41.6-89.6 89.6l0 227.2C544 432 585.6 470.4 633.6 470.4zM588.8 153.6c0-25.6 19.2-44.8 44.8-44.8l249.6 0c25.6 0 44.8 19.2 44.8 44.8l0 227.2c0 25.6-19.2 44.8-44.8 44.8l-249.6 0c-25.6 0-44.8-19.2-44.8-44.8L588.8 153.6z"  ></path>' +
    '' +
    '<path d="M700.8 192l134.4 0 0 134.4-134.4 0 0-134.4Z"  ></path>' +
    '' +
    '<path d="M572.8 716.8l137.6 0c12.8 0 22.4-9.6 22.4-22.4l0-137.6c0-12.8-9.6-22.4-22.4-22.4l-137.6 0c-12.8 0-22.4 9.6-22.4 22.4l0 137.6C550.4 707.2 560 716.8 572.8 716.8z"  ></path>' +
    '' +
    '<path d="M886.4 563.2l0 38.4c0 12.8 12.8 25.6 25.6 25.6l38.4 0c12.8 0 25.6-12.8 25.6-25.6l0-38.4c0-12.8-12.8-25.6-25.6-25.6l-38.4 0C899.2 537.6 886.4 547.2 886.4 563.2z"  ></path>' +
    '' +
    '<path d="M582.4 944l48 0c12.8 0 22.4-9.6 22.4-22.4l0-48c0-12.8-9.6-22.4-22.4-22.4l-48 0c-12.8 0-22.4 9.6-22.4 22.4l0 48C560 934.4 569.6 944 582.4 944z"  ></path>' +
    '' +
    '<path d="M944 704l-99.2 0c-16 0-28.8 12.8-28.8 28.8l0 44.8-48 0c-19.2 0-32 12.8-32 32l0 99.2c0 16 12.8 28.8 28.8 28.8l179.2 3.2c16 0 28.8-12.8 28.8-28.8l0-179.2C972.8 716.8 960 704 944 704z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-jushoucang" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M908.8 214.4c-9.6-12.8-19.2-22.4-28.8-32-112-115.2-230.4-105.6-342.4-16-9.6 6.4-19.2 16-28.8 25.6-9.6-9.6-19.2-16-28.8-25.6-112-86.4-230.4-99.2-342.4 16-9.6 9.6-19.2 19.2-25.6 32-134.4 195.2-60.8 387.2 137.6 560 44.8 38.4 89.6 73.6 137.6 102.4 16 9.6 32 19.2 44.8 28.8 9.6 6.4 12.8 9.6 19.2 9.6 3.2 3.2 6.4 3.2 12.8 6.4 3.2 3.2 9.6 3.2 16 6.4 25.6 6.4 64 3.2 89.6-12.8 3.2 0 9.6-3.2 16-9.6 12.8-6.4 28.8-16 44.8-28.8 48-28.8 92.8-64 137.6-102.4C969.6 598.4 1043.2 406.4 908.8 214.4zM736 732.8c-41.6 35.2-86.4 70.4-131.2 99.2-16 9.6-28.8 19.2-44.8 25.6-6.4 3.2-12.8 6.4-16 9.6-6.4 3.2-16 6.4-25.6 9.6-3.2 0-6.4 0-9.6 0-6.4 0-12.8 0-16 0-3.2 0-3.2 0-3.2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0-3.2 0-3.2-3.2-3.2 0-6.4-3.2-9.6-3.2-3.2-3.2-9.6-6.4-16-9.6-12.8-6.4-28.8-16-44.8-25.6-44.8-28.8-89.6-60.8-131.2-99.2-179.2-160-243.2-323.2-131.2-489.6 6.4-9.6 16-16 22.4-25.6 89.6-96 182.4-86.4 275.2-12.8 9.6 6.4 16 12.8 22.4 19.2 0 0 0 0 0 0l28.8 32c3.2 3.2 3.2 3.2 6.4 6.4 0 0 0 0 0 0l0 0c3.2-3.2 9.6-9.6 16-16 12.8-12.8 25.6-25.6 41.6-38.4 92.8-73.6 185.6-83.2 275.2 12.8 6.4 9.6 16 16 22.4 25.6C982.4 406.4 918.4 572.8 736 732.8z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-saoyisao" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M864 48l-697.6 0c-48 0-83.2 41.6-83.2 89.6l0 208c0 12.8 9.6 25.6 22.4 25.6s22.4-9.6 22.4-25.6l0-208c0-22.4 16-41.6 38.4-41.6l697.6 0c22.4 0 38.4 19.2 38.4 41.6l0 208c0 12.8 9.6 25.6 22.4 25.6s22.4-9.6 22.4-25.6l0-208C947.2 89.6 908.8 48 864 48z"  ></path>' +
    '' +
    '<path d="M924.8 681.6c-12.8 0-22.4 9.6-22.4 25.6l0 179.2c0 22.4-16 41.6-38.4 41.6l-697.6 0c-22.4 0-38.4-19.2-38.4-41.6L128 704c0-12.8-9.6-25.6-22.4-25.6s-22.4 9.6-22.4 25.6l0 179.2c0 51.2 38.4 89.6 83.2 89.6l697.6 0c48 0 83.2-41.6 83.2-89.6L947.2 704C947.2 691.2 937.6 681.6 924.8 681.6z"  ></path>' +
    '' +
    '<path d="M921.6 544c12.8 0 22.4-9.6 22.4-22.4 0-12.8-9.6-22.4-22.4-22.4l-819.2 0c-12.8 0-22.4 9.6-22.4 22.4 0 12.8 9.6 22.4 22.4 22.4L921.6 544z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-shezhi" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M512 697.6c102.4 0 182.4-83.2 182.4-185.6 0-102.4-83.2-185.6-182.4-185.6-102.4 0-182.4 83.2-182.4 185.6C329.6 614.4 412.8 697.6 512 697.6L512 697.6zM512 646.4c-73.6 0-134.4-60.8-134.4-134.4 0-73.6 60.8-134.4 134.4-134.4 73.6 0 134.4 60.8 134.4 134.4C646.4 585.6 585.6 646.4 512 646.4L512 646.4z"  ></path>' +
    '' +
    '<path d="M249.015232 843.178592c35.2 28.8 73.6 51.2 112 67.2 41.6-38.4 96-60.8 150.4-60.8 57.6 0 108.8 22.4 150.4 60.8 41.6-16 80-38.4 112-67.2-12.8-54.4-3.2-112 22.4-163.2 28.8-48 73.6-86.4 128-102.4 3.2-22.4 6.4-44.8 6.4-67.2 0-22.4-3.2-44.8-6.4-67.2-54.4-16-99.2-54.4-128-102.4-28.8-48-35.2-108.8-22.4-163.2-35.2-28.8-73.6-51.2-112-67.2-41.6 38.4-92.8 60.8-150.4 60.8-54.4 0-108.8-22.4-150.4-60.8-41.6 16-80 38.4-112 67.2 12.8 54.4 3.2 112-22.4 163.2-28.8 48-73.6 86.4-128 102.4-3.2 22.4-6.4 44.8-6.4 67.2 0 22.4 3.2 44.8 6.4 67.2 54.4 16 99.2 54.4 128 102.4C252.215232 731.178592 261.815232 788.778592 249.015232 843.178592M361.015232 958.378592c-54.4-19.2-105.6-48-150.4-89.6-6.4-6.4-9.6-16-6.4-22.4 16-48 9.6-99.2-16-140.8-25.6-44.8-64-73.6-112-83.2-9.6-3.2-16-9.6-16-19.2-6.4-28.8-9.6-60.8-9.6-89.6 0-28.8 3.2-57.6 9.6-89.6 3.2-9.6 9.6-16 16-19.2 48-12.8 89.6-41.6 112-83.2 25.6-44.8 28.8-92.8 16-140.8-3.2-9.6 0-19.2 6.4-22.4 44.8-38.4 96-67.2 150.4-89.6 9.6-3.2 16 0 22.4 6.4 35.2 35.2 80 57.6 128 57.6 48 0 96-19.2 128-57.6 6.4-6.4 16-9.6 22.4-6.4 54.4 19.2 105.6 48 150.4 89.6 6.4 6.4 9.6 16 6.4 22.4-16 48-9.6 99.2 16 140.8 25.6 44.8 64 73.6 112 83.2 9.6 3.2 16 9.6 16 19.2 6.4 28.8 9.6 60.8 9.6 89.6 0 28.8-3.2 57.6-9.6 89.6-3.2 9.6-9.6 16-16 19.2-48 12.8-89.6 41.6-112 83.2-25.6 44.8-28.8 92.8-16 140.8 3.2 9.6 0 19.2-6.4 22.4-44.8 38.4-96 67.2-150.4 89.6-9.6 3.2-16 0-22.4-6.4-35.2-35.2-80-57.6-128-57.6-48 0-96 19.2-128 57.6-3.2 3.2-9.6 6.4-16 6.4C364.215232 958.378592 361.015232 958.378592 361.015232 958.378592z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-shouhuodizhi" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M518.4 48c-214.4 0-390.4 176-390.4 393.6 0 48 16 99.2 41.6 156.8 28.8 57.6 70.4 118.4 118.4 182.4 35.2 41.6 73.6 83.2 108.8 121.6 12.8 12.8 25.6 25.6 35.2 35.2 6.4 6.4 12.8 9.6 12.8 12.8l0 0c38.4 38.4 102.4 38.4 137.6 0 3.2-3.2 6.4-6.4 12.8-12.8 9.6-9.6 22.4-22.4 35.2-35.2 38.4-38.4 73.6-80 108.8-121.6 51.2-60.8 92.8-124.8 118.4-182.4 28.8-57.6 41.6-108.8 41.6-156.8C908.8 224 732.8 48 518.4 48zM822.4 576c-25.6 54.4-64 112-115.2 172.8-35.2 41.6-70.4 83.2-105.6 118.4-12.8 12.8-25.6 25.6-35.2 35.2-6.4 6.4-9.6 9.6-12.8 12.8-19.2 19.2-51.2 19.2-70.4 0l0 0c-3.2-3.2-6.4-6.4-12.8-12.8-9.6-9.6-22.4-22.4-35.2-35.2-35.2-38.4-73.6-76.8-105.6-118.4-48-60.8-86.4-118.4-115.2-172.8-25.6-51.2-38.4-96-38.4-134.4 0-192 153.6-345.6 342.4-345.6 188.8 0 342.4 153.6 342.4 345.6C860.8 480 848 524.8 822.4 576z"  ></path>' +
    '' +
    '<path d="M518.4 262.4c-96 0-169.6 76.8-169.6 172.8 0 96 76.8 172.8 169.6 172.8s169.6-76.8 169.6-172.8C688 339.2 614.4 262.4 518.4 262.4zM518.4 556.8c-67.2 0-121.6-54.4-121.6-124.8s54.4-124.8 121.6-124.8c67.2 0 121.6 54.4 121.6 124.8S585.6 556.8 518.4 556.8z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-shouye" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M969.6 502.4l-118.4-112-323.2-300.8c-9.6-9.6-22.4-9.6-32 0l-313.6 297.6c-3.2 3.2-6.4 6.4-9.6 9.6l-118.4 112c-9.6 9.6-9.6 22.4 0 32s22.4 9.6 32 0l83.2-80 0 393.6c0 48 41.6 89.6 92.8 89.6l83.2 0c38.4 0 70.4-28.8 70.4-67.2l0-217.6 99.2 0 99.2 0 0 217.6c0 35.2 32 67.2 70.4 67.2l83.2 0c51.2 0 92.8-38.4 92.8-89.6l0-396.8 80 73.6c9.6 9.6 22.4 9.6 32 0C979.2 524.8 979.2 512 969.6 502.4zM809.6 857.6c0 25.6-19.2 44.8-44.8 44.8l-83.2 0c-12.8 0-22.4-9.6-22.4-22.4L659.2 640c0-12.8-9.6-22.4-22.4-22.4l-121.6 0-121.6 0c-12.8 0-22.4 9.6-22.4 22.4l0 240c0 12.8-9.6 22.4-22.4 22.4l-83.2 0c-25.6 0-44.8-19.2-44.8-44.8l0-438.4 294.4-281.6 294.4 281.6L809.6 857.6z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-shuaxin" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M960 630.4c-12.8-3.2-25.6 3.2-32 12.8-76.8 204.8-320 307.2-544 227.2-224-80-342.4-307.2-265.6-512 76.8-204.8 320-307.2 544-227.2 92.8 32 172.8 92.8 224 172.8l-92.8 0c-12.8 0-25.6 9.6-25.6 22.4 0 12.8 9.6 22.4 25.6 22.4l153.6 0c12.8 0 25.6-9.6 25.6-22.4l0-140.8c0-12.8-9.6-22.4-25.6-22.4-12.8 0-25.6 9.6-25.6 22.4l0 89.6c-57.6-86.4-140.8-150.4-246.4-188.8-249.6-86.4-518.4 28.8-608 256-86.4 230.4 44.8 486.4 294.4 572.8 249.6 86.4 518.4-28.8 608-256C979.2 649.6 972.8 636.8 960 630.4z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-sousuo" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M966.4 924.8l-230.4-227.2c60.8-67.2 96-156.8 96-256 0-217.6-176-390.4-390.4-390.4-217.6 0-390.4 176-390.4 390.4 0 217.6 176 390.4 390.4 390.4 99.2 0 188.8-35.2 256-96l230.4 227.2c9.6 9.6 28.8 9.6 38.4 0C979.2 950.4 979.2 934.4 966.4 924.8zM102.4 441.6c0-185.6 150.4-339.2 339.2-339.2s339.2 150.4 339.2 339.2c0 89.6-35.2 172.8-92.8 233.6-3.2 0-3.2 3.2-6.4 3.2-3.2 3.2-3.2 3.2-3.2 6.4-60.8 57.6-144 92.8-233.6 92.8C256 780.8 102.4 627.2 102.4 441.6z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-tishi" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M508.8 44.8c-256 0-464 208-464 464s208 464 464 464 464-208 464-464S764.8 44.8 508.8 44.8zM508.8 924.8c-230.4 0-416-185.6-416-416s185.6-416 416-416 416 185.6 416 416S739.2 924.8 508.8 924.8z"  ></path>' +
    '' +
    '<path d="M521.6 652.8c12.8 0 22.4-9.6 22.4-22.4l0-428.8c0-12.8-9.6-22.4-22.4-22.4-12.8 0-22.4 9.6-22.4 22.4l0 428.8C496 640 508.8 652.8 521.6 652.8z"  ></path>' +
    '' +
    '<path d="M521.6 748.8m-35.2 0a1.1 1.1 0 1 0 70.4 0 1.1 1.1 0 1 0-70.4 0Z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-wancheng" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M486.4 630.4c-19.2 19.2-48 19.2-67.2 3.2l-137.6-131.2-32 35.2 137.6 131.2c38.4 35.2 96 35.2 134.4-3.2l281.6-297.6-35.2-32L486.4 630.4z"  ></path>' +
    '' +
    '<path d="M512 51.2c-252.8 0-460.8 204.8-460.8 460.8s204.8 460.8 460.8 460.8 460.8-204.8 460.8-460.8S764.8 51.2 512 51.2zM512 924.8c-227.2 0-412.8-185.6-412.8-412.8s185.6-412.8 412.8-412.8 412.8 185.6 412.8 412.8S739.2 924.8 512 924.8z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-wodedingdan" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M883.2 60.8l-89.6 0c-12.8 0-25.6 9.6-25.6 25.6s9.6 25.6 25.6 25.6l89.6 0c25.6 0 48 22.4 48 48l0 713.6c0 25.6-22.4 48-48 48l-736 0c-25.6 0-48-22.4-48-48l0-713.6c0-25.6 22.4-48 48-48l99.2 0c12.8 0 25.6-9.6 25.6-25.6s-9.6-25.6-25.6-25.6l-99.2 0c-54.4 0-96 41.6-96 96l0 713.6c0 54.4 41.6 96 96 96l736 0c54.4 0 96-41.6 96-96l0-713.6C979.2 102.4 934.4 60.8 883.2 60.8z"  ></path>' +
    '' +
    '<path d="M393.6 108.8l240 0c12.8 0 25.6-9.6 25.6-25.6s-9.6-25.6-25.6-25.6l-240 0c-12.8 0-25.6 9.6-25.6 25.6S380.8 108.8 393.6 108.8z"  ></path>' +
    '' +
    '<path d="M294.4 345.6l464 0c12.8 0 25.6-9.6 25.6-25.6s-9.6-25.6-25.6-25.6l-464 0c-12.8 0-25.6 9.6-25.6 25.6S278.4 345.6 294.4 345.6z"  ></path>' +
    '' +
    '<path d="M294.4 540.8l464 0c12.8 0 25.6-9.6 25.6-25.6s-9.6-25.6-25.6-25.6l-464 0c-12.8 0-25.6 9.6-25.6 25.6S278.4 540.8 294.4 540.8z"  ></path>' +
    '' +
    '<path d="M294.4 736l464 0c12.8 0 25.6-9.6 25.6-25.6s-9.6-25.6-25.6-25.6l-464 0c-12.8 0-25.6 9.6-25.6 25.6S278.4 736 294.4 736z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-wodefankui" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M883.2 83.2l-742.4 0c-51.2 0-92.8 41.6-92.8 89.6l0 582.4c0 48 41.6 89.6 92.8 89.6l256 0c0 0 6.4 6.4 16 19.2 3.2 3.2 6.4 6.4 9.6 12.8 0 0 6.4 9.6 9.6 12.8 28.8 44.8 51.2 67.2 86.4 67.2 35.2 0 60.8-22.4 89.6-67.2 22.4-35.2 28.8-44.8 28.8-44.8l249.6 0c51.2 0 92.8-41.6 92.8-89.6l0-582.4C976 121.6 934.4 83.2 883.2 83.2zM931.2 755.2c0 25.6-19.2 44.8-44.8 44.8l-252.8 0c-6.4 0-9.6 0-16 3.2-9.6 6.4-19.2 12.8-28.8 28.8-3.2 6.4-22.4 35.2-22.4 35.2-19.2 32-35.2 44.8-48 44.8-12.8 0-25.6-12.8-48-44.8-3.2-3.2-6.4-12.8-9.6-12.8-3.2-6.4-6.4-9.6-9.6-12.8-19.2-25.6-32-38.4-54.4-38.4l-256 0c-25.6 0-44.8-19.2-44.8-44.8l0-582.4c0-25.6 22.4-44.8 48-44.8l742.4 0c25.6 0 48 19.2 48 44.8L934.4 755.2z"  ></path>' +
    '' +
    '<path d="M220.8 483.2a1.6 1.6 0 1 0 102.4 0 1.6 1.6 0 1 0-102.4 0Z"  ></path>' +
    '' +
    '<path d="M460.8 483.2a1.6 1.6 0 1 0 102.4 0 1.6 1.6 0 1 0-102.4 0Z"  ></path>' +
    '' +
    '<path d="M697.6 483.2a1.6 1.6 0 1 0 102.4 0 1.6 1.6 0 1 0-102.4 0Z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-wodehongbao" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M865.745455 102.4C865.745455 102.4 865.745455 102.4 865.745455 102.4 865.745455 99.29697 865.745455 99.29697 865.745455 102.4c-18.618182-24.824242-46.545455-40.339394-77.575758-40.339394l-574.060606 0c-55.854545 0-99.29697 43.442424-99.29697 99.29697l0 698.181818c0 55.854545 43.442424 99.29697 99.29697 99.29697l574.060606 0c55.854545 0 99.29697-43.442424 99.29697-99.29697l0-698.181818C887.466667 139.636364 881.260606 117.915152 865.745455 102.4zM788.169697 111.709091c3.10303 0 9.309091 0 12.412121 3.10303l-291.684848 186.181818-313.406061-186.181818c6.206061-3.10303 12.412121-3.10303 18.618182-3.10303L788.169697 111.709091zM837.818182 862.642424c0 27.927273-21.721212 49.648485-49.648485 49.648485l-574.060606 0c-27.927273 0-49.648485-21.721212-49.648485-49.648485l0-698.181818c0-3.10303 0-3.10303 0-6.206061l332.024242 195.490909c9.309091 6.206061 18.618182 3.10303 24.824242 0l316.509091-201.69697c0 3.10303 3.10303 9.309091 3.10303 12.412121L840.921212 862.642424z"  ></path>' +
    '' +
    '<path d="M639.224242 561.648485c12.412121 0 24.824242-12.412121 24.824242-24.824242 0-12.412121-12.412121-24.824242-24.824242-24.824242l-83.781818 0 71.369697-71.369697c9.309091-9.309091 9.309091-24.824242 0-34.133333-9.309091-9.309091-24.824242-9.309091-34.133333 0l-80.678788 80.678788c0 0-3.10303 0-3.10303 0 0 0 0 0 0 0l-77.575758-77.575758c-9.309091-9.309091-24.824242-9.309091-34.133333 0-9.309091 9.309091-9.309091 24.824242 0 34.133333l68.266667 68.266667-74.472727 0c-12.412121 0-24.824242 12.412121-24.824242 24.824242 0 12.412121 12.412121 24.824242 24.824242 24.824242l93.090909 0 0 74.472727-93.090909 0c-12.412121 0-24.824242 12.412121-24.824242 24.824242 0 12.412121 12.412121 24.824242 24.824242 24.824242l93.090909 0 0 89.987879c0 12.412121 12.412121 24.824242 24.824242 24.824242 12.412121 0 24.824242-12.412121 24.824242-24.824242l0-89.987879 108.606061 0c12.412121 0 24.824242-12.412121 24.824242-24.824242 0-12.412121-12.412121-24.824242-24.824242-24.824242l-108.606061 0 0-74.472727L639.224242 561.648485z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-wodejuhuasuan" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M620.8 486.4c54.4-38.4 92.8-102.4 92.8-172.8 0-118.4-96-211.2-211.2-211.2-115.2 0-211.2 96-211.2 211.2 0 73.6 38.4 140.8 96 176-204.8 48-355.2 214.4-339.2 406.4 0 0 0-3.2 0 0l0 0c0 0 0 9.6 3.2 12.8 3.2 6.4 12.8 12.8 19.2 12.8l880 0c9.6 0 19.2-9.6 22.4-22.4l0 0 0 0c0-6.4 0 0 0 0C992 697.6 832 528 620.8 486.4zM336 310.4c0-89.6 73.6-163.2 163.2-163.2 89.6 0 163.2 73.6 163.2 163.2s-73.6 163.2-163.2 163.2C409.6 473.6 336 400 336 310.4zM96 870.4c-9.6-192 179.2-348.8 412.8-348.8l3.2 0c233.6 0 422.4 153.6 416 348.8L96 870.4z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-wodeyouhuiquan" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M876.8 166.4l-726.4 0c-57.6 0-102.4 44.8-102.4 102.4l0 118.4 0 0 0 48c0 0 22.4 0 25.6 0 25.6 0 51.2 22.4 51.2 54.4 0 28.8-22.4 54.4-51.2 54.4-6.4 0-25.6 0-25.6 0l0 44.8 0 0 0 6.4 0 118.4c0 57.6 44.8 102.4 102.4 102.4l726.4 0c57.6 0 102.4-44.8 102.4-102.4l0-118.4 0-6.4 0-44.8c0 0-19.2 0-22.4 0-25.6 0-51.2-22.4-51.2-54.4 0-28.8 22.4-54.4 51.2-54.4 6.4 0 25.6 0 22.4 0L979.2 384l0-3.2 0-115.2C976 211.2 931.2 166.4 876.8 166.4zM851.2 486.4c0 48 32 86.4 73.6 99.2l0 121.6c0 28.8-22.4 51.2-51.2 51.2l-726.4 0c-28.8 0-51.2-22.4-51.2-51.2l0-118.4 0 0c51.2-6.4 76.8-54.4 76.8-102.4 0-48-32-92.8-76.8-102.4l0-115.2c0-28.8 22.4-51.2 51.2-51.2l726.4 0c28.8 0 51.2 22.4 51.2 51.2l0 118.4C883.2 396.8 851.2 438.4 851.2 486.4z"  ></path>' +
    '' +
    '<path d="M614.4 489.6c12.8 0 25.6-12.8 25.6-25.6 0-12.8-12.8-25.6-25.6-25.6l-67.2 0 57.6-57.6c9.6-9.6 9.6-25.6 0-35.2-9.6-9.6-25.6-9.6-35.2 0l-57.6 57.6-57.6-57.6c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 25.6 0 35.2l57.6 57.6-64 0c-12.8 0-25.6 12.8-25.6 25.6 0 12.8 12.8 25.6 25.6 25.6l80 0 0 51.2-80 0c-12.8 0-25.6 12.8-25.6 25.6 0 12.8 12.8 25.6 25.6 25.6l80 0 0 51.2c0 12.8 12.8 25.6 25.6 25.6 12.8 0 25.6-12.8 25.6-25.6l0-51.2 76.8 0c12.8 0 25.6-12.8 25.6-25.6 0-12.8-12.8-25.6-25.6-25.6l-76.8 0 0-51.2L614.4 489.6z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-xiafan" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M921.6 563.2c-9.6-9.6-25.6-9.6-35.2 0L544 896l0-822.4c0-12.8-9.6-22.4-25.6-22.4s-25.6 9.6-25.6 22.4L492.8 896l-342.4-339.2c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 22.4 0 32l384 377.6c6.4 6.4 12.8 6.4 19.2 6.4 0 0 0 0 0 0 3.2 0 3.2 0 6.4 0 0 0 0 0 3.2 0 3.2 0 6.4-3.2 9.6-6.4l380.8-371.2C931.2 588.8 931.2 572.8 921.6 563.2z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-xiangshangjiantou" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M966.4 668.8l-435.2-432c-9.6-9.6-25.6-9.6-35.2 0l-441.6 432c-9.6 9.6-9.6 25.6 0 35.2 9.6 9.6 25.6 9.6 35.2 0l425.6-416 416 416c9.6 9.6 25.6 9.6 35.2 0S976 678.4 966.4 668.8z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-xiangxiajiantou" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M966.4 323.2c-9.6-9.6-25.6-9.6-35.2 0l-416 416-425.6-416c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 25.6 0 35.2l441.6 432c9.6 9.6 25.6 9.6 35.2 0l435.2-432C976 345.6 976 332.8 966.4 323.2z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-xiangyoujiantou" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M761.6 489.6l-432-435.2c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 25.6 0 35.2l416 416-416 425.6c-9.6 9.6-9.6 25.6 0 35.2s25.6 9.6 35.2 0l432-441.6C771.2 515.2 771.2 499.2 761.6 489.6z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-xiangzuojiantou" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M729.6 931.2l-416-425.6 416-416c9.6-9.6 9.6-25.6 0-35.2-9.6-9.6-25.6-9.6-35.2 0l-432 435.2c-9.6 9.6-9.6 25.6 0 35.2l432 441.6c9.6 9.6 25.6 9.6 35.2 0C739.2 956.8 739.2 940.8 729.6 931.2z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-yijianfankui" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M873.6 275.2c-16 0-25.6 12.8-25.6 25.6l0 550.4c0 28.8-22.4 51.2-51.2 51.2l-643.2 0c-28.8 0-51.2-22.4-51.2-51.2l0-646.4c0-28.8 22.4-51.2 51.2-51.2l521.6 0c16 0 25.6-12.8 25.6-25.6 0-12.8-12.8-25.6-25.6-25.6l-521.6 0c-57.6 0-105.6 48-105.6 102.4l0 646.4c0 57.6 48 102.4 105.6 102.4l643.2 0c57.6 0 105.6-44.8 105.6-102.4l0-550.4C899.2 284.8 889.6 275.2 873.6 275.2z"  ></path>' +
    '' +
    '<path d="M483.2 361.6l-278.4 0c-16 0-25.6 12.8-25.6 25.6 0 12.8 12.8 25.6 25.6 25.6l278.4 0c16 0 25.6-12.8 25.6-25.6C508.8 371.2 496 361.6 483.2 361.6z"  ></path>' +
    '' +
    '<path d="M179.2 566.4c0 12.8 12.8 25.6 25.6 25.6l460.8 0c16 0 25.6-12.8 25.6-25.6 0-12.8-12.8-25.6-25.6-25.6l-460.8 0C192 540.8 179.2 553.6 179.2 566.4z"  ></path>' +
    '' +
    '<path d="M969.6 57.6c-9.6-9.6-25.6-9.6-38.4 0l-326.4 323.2c-9.6 9.6-9.6 25.6 0 35.2s25.6 9.6 38.4 0l326.4-323.2C979.2 83.2 979.2 67.2 969.6 57.6z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-xiaoxizhongxin" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M585.6 905.6 585.6 905.6c25.6-32 38.4-44.8 41.6-44.8 208-38.4 345.6-198.4 345.6-396.8 3.2-227.2-204.8-406.4-460.8-406.4s-464 179.2-464 406.4c0 198.4 140.8 358.4 345.6 396.8 3.2 0 16 12.8 41.6 44.8l0 0c32 41.6 51.2 64 73.6 64S550.4 947.2 585.6 905.6M620.8 803.2c-19.2 3.2-32 19.2-67.2 64l0 0c-22.4 25.6-41.6 44.8-41.6 44.8l-9.6-9.6c-6.4-6.4-16-19.2-25.6-32-38.4-48-51.2-64-70.4-67.2-185.6-32-307.2-172.8-307.2-345.6 0-192 185.6-352 416-352s416 160 416 352C928 633.6 806.4 771.2 620.8 803.2z"  ></path>' +
    '' +
    '<path d="M281.6 470.4a1.4 1.5 0 1 0 89.6 0 1.4 1.5 0 1 0-89.6 0Z"  ></path>' +
    '' +
    '<path d="M467.2 470.4a1.4 1.5 0 1 0 89.6 0 1.4 1.5 0 1 0-89.6 0Z"  ></path>' +
    '' +
    '<path d="M652.8 470.4a1.4 1.5 0 1 0 89.6 0 1.4 1.5 0 1 0-89.6 0Z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-jushoucanggift" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M892.543016 224.150106c-9.284457-11.914354-17.804505-21.814842-26.454512-30.930453C759.437485 80.827887 642.682341 92.003414 536.033369 176.799682c-9.493212 7.547907-18.453281 15.383362-26.88737 23.346731-8.43409-7.963369-17.395182-15.798824-26.888394-23.346731C375.608633 92.003414 258.853489 80.827887 152.202471 193.21863c-8.650007 9.115612-17.170055 19.016099-25.559119 29.714765C-2.680039 410.134984 68.411089 595.897805 259.728416 764.030084c42.320874 37.192064 87.560218 70.64906 132.799562 99.905384 15.841803 10.245342 30.570249 19.244296 43.816948 26.932396 8.024767 4.657067 13.827937 7.872295 17.044188 9.578146 4.869914 2.916423 9.728572 5.142114 14.530948 6.771217 3.470031 1.619894 7.516184 3.091408 12.218276 4.387937 25.377994 6.998391 62.97938 1.908466 85.839017-11.764951 2.14178-1.101077 7.944949-4.315282 15.969717-8.972349 13.246699-7.688099 27.974122-16.687054 43.816948-26.932396 45.239344-29.256324 90.478687-62.71332 132.799562-99.905384C949.879885 595.897805 1020.971014 410.134984 892.543016 224.150106z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '</svg>'
  var script = function() {
    var scripts = document.getElementsByTagName('script')
    return scripts[scripts.length - 1]
  }()
  var shouldInjectCss = script.getAttribute("data-injectcss")

  /**
   * document ready
   */
  var ready = function(fn) {
    if (document.addEventListener) {
      if (~["complete", "loaded", "interactive"].indexOf(document.readyState)) {
        setTimeout(fn, 0)
      } else {
        var loadFn = function() {
          document.removeEventListener("DOMContentLoaded", loadFn, false)
          fn()
        }
        document.addEventListener("DOMContentLoaded", loadFn, false)
      }
    } else if (document.attachEvent) {
      IEContentLoaded(window, fn)
    }

    function IEContentLoaded(w, fn) {
      var d = w.document,
        done = false,
        // only fire once
        init = function() {
          if (!done) {
            done = true
            fn()
          }
        }
        // polling for no errors
      var polling = function() {
        try {
          // throws errors until after ondocumentready
          d.documentElement.doScroll('left')
        } catch (e) {
          setTimeout(polling, 50)
          return
        }
        // no errors, fire

        init()
      };

      polling()
        // trying to always fire before onload
      d.onreadystatechange = function() {
        if (d.readyState == 'complete') {
          d.onreadystatechange = null
          init()
        }
      }
    }
  }

  /**
   * Insert el before target
   *
   * @param {Element} el
   * @param {Element} target
   */

  var before = function(el, target) {
    target.parentNode.insertBefore(el, target)
  }

  /**
   * Prepend el to target
   *
   * @param {Element} el
   * @param {Element} target
   */

  var prepend = function(el, target) {
    if (target.firstChild) {
      before(el, target.firstChild)
    } else {
      target.appendChild(el)
    }
  }

  function appendSvg() {
    var div, svg

    div = document.createElement('div')
    div.innerHTML = svgSprite
    svgSprite = null
    svg = div.getElementsByTagName('svg')[0]
    if (svg) {
      svg.setAttribute('aria-hidden', 'true')
      svg.style.position = 'absolute'
      svg.style.width = 0
      svg.style.height = 0
      svg.style.overflow = 'hidden'
      prepend(svg, document.body)
    }
  }

  if (shouldInjectCss && !window.__iconfont__svg__cssinject__) {
    window.__iconfont__svg__cssinject__ = true
    try {
      document.write("<style>.svgfont {display: inline-block;width: 1em;height: 1em;fill: currentColor;vertical-align: -0.1em;font-size:16px;}</style>");
    } catch (e) {
      console && console.log(e)
    }
  }

  ready(appendSvg)


})(window)