内容推送微服务系统
==========

## 项目简介
用于接收审核平台发送的指定内容，生成特定格式并推送到指定系统的一个微服务系统。目前仅用于推送至新华网CMS。

该系统支持部署于docker容器中。


## 项目状态
上线 IP 192.168.84.157
机器故障 暂时部署在192.168.84.154



## 项目团队情况
1. 项目组成员
@gongchengdong
@chenweichao
2. 联系电话
50707


## docker部署说明

1. 打runable jar  

	    mvn clean package -Dmaven.test.skip

2. 构建镜像   

	    docker build -t="xhndev/xh-cms-pusher:0.1" .

3. 导出导入镜像   
	
	    sudo docker save -o xh-cms-pusher.tar xhndev/xh-cms-pusher:0.1
	    sudo docker load < xh-cms-pusher.tar

4. 运行容器          

            #*注意容器名和挂载目录名xh-cms-pusher_01 是唯一的*
            docker run -d -P --name xh-cms-pusher_01 -v /home/admin/xh-cms-pusher_01:/tmp/xh-cms-pusher/ xhndev/xh-cms-pusher:0.1
