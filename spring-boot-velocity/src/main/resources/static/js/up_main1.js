$(function() {

    $('#up_bt').addClass('animated fadeInDown').css('display','block');
    $('#up_share').addClass('animated fadeInUp').css('display','block').click(function(){
        window.location.href = shareUrl;
    });
    $('#up_return').addClass('animated fadeInUp').css('display','block').click(function(){
        window.location.href = 'http://fms.news.cn/swf/3_1_lhyb/index.html';
    });

    function toFixed2(num) {
        return parseFloat(+num.toFixed(2));
    }

    $('#cancleBtn').on('click', function() {
        $("#showEdit").fadeOut();
        $('#showResult').fadeIn();
    });

    var shareUrl = 'http://fms.news.cn/swf/3_1_lhyb/fx/index1.html?';

    $('#confirmBtn').on('click', function() {
        $("#showEdit").fadeOut();

        var $image = $('#report > img');
        var dataURL = $image.cropper("getCroppedCanvas");
        var imgurl = dataURL.toDataURL("image/jpeg", 0.5);
        $("#changeAvatar > img").attr("src", imgurl);
        $("#basetxt").val(imgurl);
        $('#showResult').fadeIn();

        //ajax
        var content = $('#changeAvatar img').attr('src');
        $.ajax({
            type: "POST",
            url: "/lhyb_up/upload",
            dataType:"json",
            //json:"callback",
            data: {type:1,fileName:$('#image').val(),base64Img:content},
            success: function(msg){
                //console.log($('#image').files[0]);
                console.log(msg.data.uuid);
                shareUrl += msg.data.uuid;

            },
            error:function(msg){
                //console.log($('#image').files[0]);
            }
        });
    });

    function cutImg() {
        $("#showEdit").fadeIn();
        var $image = $('#report > img');
        $image.cropper({
            aspectRatio: 1/1,
            autoCropArea: 0.3,
            strict: true,
            guides: false,
            center: true,
            highlight: false,
            dragCrop: false,
            cropBoxMovable: false,
            cropBoxResizable: false,
            zoom: -0.2,
            checkImageOrigin: true,
            background: false,
            minContainerHeight: 400,
            minContainerWidth: 300
        });
    }

    function doFinish(startTimestamp, sSize, rst) {
        var finishTimestamp = (new Date()).valueOf();
        var elapsedTime = (finishTimestamp - startTimestamp);
        //$('#elapsedTime').text('ѹ����ʱ�� ' + elapsedTime + 'ms');

        var sourceSize = toFixed2(sSize / 1024),
            resultSize = toFixed2(rst.base64Len / 1024),
            scale = parseInt(100 - (resultSize / sourceSize * 100));
        $("#report").html('<img src="' + rst.base64 + '" style="width: 100%;height:100%">');
        cutImg();
    }

    $('#image').on('change', function() {
        var startTimestamp = (new Date()).valueOf();
        var that = this;
        lrz(this.files[0], {
            width: 800,
            height: 800,
            quality: 0.7
        })
            .then(function(rst) {
                //console.log(rst);
                doFinish(startTimestamp, that.files[0].size, rst);
                return rst;
            })
            .then(function(rst) {
                // ������ϴ�������
                return rst;
            })
            .then(function(rst) {
                // �����Ҫ��һֱthen��ȥ����
            })
            .catch(function(err) {
                // ��һ�����ˣ�������Բ�׽��������Ϣ
                // �������ϵ�then������ִ��
                alert(err);
            })
            .always(function() {
                // �����ǳɹ�ʧ�ܣ����ﶼ��ִ��
            });
    });
});