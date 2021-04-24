关于
需要一个发送端 (driver):
    发送程序
    接收结果

需要接收端:
    接收程序
    分配资源
    创建 worker
    启动 worker

简化:
    driver 请求 server
        发送函数 和参数
        等待结果
    server
        接收请求, 获取函数 和参数
        创建worker(线程/ 进程)
        启动worker任务
        等待worker完成
        返回结果
    driver 收到结果
    结束

-----

driver
    submit
        start connect host port
        send func
        wait recv

session
    listen port
        get func
        start thread
            run func
            send response
            over

