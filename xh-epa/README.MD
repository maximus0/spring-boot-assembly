企业认证微服务系统
==========

## 项目简介
订阅企业认证相关消息，更新用户中心资料的一个微服务系统。

该系统支持部署于docker容器中。


## 项目状态
未上线



## 项目团队情况
1. 项目组成员
@gongchengdong
@chenweichao
@zhaodm
2. 联系电话
50707


## docker部署说明

1. 打runable jar
    mvn clean package -Dmaven.test.skip

2. 构建镜像
    docker build -t="xhndev/xh-epa:0.1" .

3. 导出导入镜像
    sudo docker save -o xh-epa.tar xhndev/xh-epa:0.1
    sudo docker load < xh-epa.tar

4. 运行容器
    *注意容器名和挂载目录名xh-xh-epa_01 是唯一的*
    docker run -d -P --name xh-epa_01 -v /home/admin/xh-epa_01:/tmp/xh-epa/ xhndev/xh-epa:0.1



