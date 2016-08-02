#com.tlw.download(tlw'的断点续传软件)

##Brief（简介):

Broken point continue download tools, by tlw_ray@163.com on 2016-8.

断点续传下载工具，由tlw_ray@163.com于2016年8月上传。

##Runtime Environment(运行环境):

JRE6+

##Usage(使用说明): 

download

 -path <path>   Download file to path(下载文件到路径).
 -task <task>   Download thread count(下载线程数量).
 -url <url>     Download file's URL(要下载文件的URL).


example(示例):

	download.bat -url http://www.softwares.com/some.zip
	
##Implemention(实现):

####1. Class(类):

#####1.1 FileInfo下载文件信息，包含文件名、HTTP状态、文件长度。

#####1.2 Splitter多线程下载，用来下载该文件的一段。

#####1.3 Task下载任务，通过制定要下载的URL,下载后存贮的位置,分几段下载来定义任务。

####2. Flow(流程):

#####2.1 参数检查：

######2.1.1 -url 为必填参数且为http://开头。

#####2.1.2 -task 是可选参数，默认值是5，如果填写必须符合0<task<256。

#####2.1.3 -path 是可选参数，如果不填则表示下载到当前目录，否则表示下载到指定目录。

#####2.2 获取任务信息(Task):

######2.2.1 获取文件名，有的下载链接是转跳过的例如:http://xxx/xx?id=xxx，转跳后会返回文件名。

######2.2.2 获取文件长度，用于支持断点续传。

######2.2.3 获取HTTP状态，如果状态大于HTTP_400即Bad Request，则认为出错并报错，否则说明获取文件信息成功。

######2.2.4 根据文件长度和线程数计算开启多线程下载文件的不同段落。

#####2.3 多线程下载(Splitter):

######2.3.1 多线程下载文件中某一段。

######2.3.2 下载过程中出现网络超时或者其它原因则重新连接继续下载，并记录日志。（这里可以优化改进，继续分段下载)

######2.3.3 所有线程下载完毕后主线程结束，下载完毕。
