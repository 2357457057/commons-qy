import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author YYJ
 * @version 1.0.0 * @ClassName PACKAGE_NAME.TestTable
 * @description
 * @createTime 2023年02月19日 03:30:00
 */
public class TestTable {
    public static void main(String[] args) throws IOException {
//        ServerSocket serverSocket = new ServerSocket(4736);
//        while (true) {
//            Socket accept = serverSocket.accept();
//                InputStream inputStream = accept.getInputStream();
//                OutputStream outputStream = accept.getOutputStream();
//            while (true){
//                try {
//                    byte[] bytes = new byte[8];
//                    inputStream.read(bytes);
//                    outputStream.write(s.getBytes(StandardCharsets.UTF_8));
//                    outputStream.flush();
//                }catch (Exception e){
//                    break;
//                }
//
//            }
//        }
        System.out.println(s.getBytes(StandardCharsets.UTF_8).length);
    }

    static String s = "附录结构图\n" +
            "\n" +
            "![4ddf58a99f604fe1a909c4c1627cc457](/blog/img/linux_command/4ddf58a99f604fe1a909c4c1627cc457.png)\n" +
            "\n" +
            "# 1.帮助命令\n" +
            "\n" +
            "## 1.1 help命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： 命令 --help\n" +
            "#作用: 查看某个命令的帮助信息\n" +
            "# 示例: \n" +
            "    # ls --help     查看ls命令的帮助信息\n" +
            "    # netstat --help    查看netstat命令的帮助信息\n" +
            "```\n" +
            "\n" +
            "## 1.2 man命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： man 命令\n" +
            "#作用: 查看某个命令的帮助手册\n" +
            "# 示例: \n" +
            "    # man ls        #查看ls命令的帮助手册\n" +
            "    # man netstat   #查看netstat命令的帮助手册\n" +
            "```\n" +
            "\n" +
            "# 2.路径切换及查看\n" +
            "\n" +
            "## 2.1 cd命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： cd 目录\n" +
            "#作用: 切换到目录中\n" +
            "# 示例: \n" +
            "    # cd /opt       切换到/opt目录下\n" +
            "    # cd ~  切换到用户目录\n" +
            "    # cd -  切换到上一次访问的目录\n" +
            "    # cd .. 切换到上一次所在的目录\n" +
            "```\n" +
            "\n" +
            "## 2.2 pwd命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： pwd\n" +
            "#作用: 查看当前所在路径\n" +
            "# 示例: \n" +
            "    # pwd   查看当前路径，会将当前路径回显\n" +
            "```\n" +
            "\n" +
            "## 2.3 ls命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： ls [-la] [文件/目录]\n" +
            "#作用: 查看当前路径下的文件和目录，若后带有文件或目录，则只查看当前文件或目录\n" +
            "# 示例: \n" +
            "    # ls   查看当前路径下所有的文件或目录\n" +
            "    # ls -l 查看当前路径下所有的文件或目录的详细信息\n" +
            "    # ls -a 查看当前路径下所有的文件或目录,将隐藏文件显示出来\n" +
            "    # ls -l a.log   查看当前路径下a.log下的详细信息\n" +
            "```\n" +
            "\n" +
            "## 2.4 find命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： find [路径] [参数] [匹配模式]\n" +
            "#作用: 可以根据给定的路径和表达式查找的文件或目录\n" +
            "# 示例: \n" +
            "    # find / -name \"*.txt\"    查询根目录下所有以.txt结尾的文件 。\n" +
            "    # find /test -perm  644   查询/test目录下权限为644的所有文件\n" +
            "    # find . -type f     查询当前目录下所有的文件\n" +
            "    # find . -type f -name \"abc\"    查询当前目录下所有文件中包含abc字符的文件\n" +
            "    # find . -type f | sort         查询当前目录下所有文件并排序\n" +
            "    # find . -type d                查询当前目录下所有目录\n" +
            "    # find . -size 10M\n" +
            "```\n" +
            "\n" +
            "# 3.文件|目录操作\n" +
            "\n" +
            "## 3.1 文件和目录的基本操作\n" +
            "\n" +
            "### 3.1.1 touch命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： touch 文件名\n" +
            "#作用: 创建一个文件\n" +
            "# 示例: \n" +
            "    # touch a.log     创建一个a.log文件。\n" +
            "```\n" +
            "\n" +
            "### 3.1.2 ln命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： \n" +
            "    ln 源文件名 硬链接文件名\n" +
            "    ln -s 源文件名 软连接文件名\n" +
            "#作用: 创建文件链接\n" +
            "# 示例: \n" +
            "    # ln a.txt a.txt.link     为a.txt创建一个硬链接文件a.txt.link。\n" +
            "    # ln -s a.txt a.txt.link  为a.txt创建一个软连接文件 。\n" +
            "    \n" +
            "# 备注：\n" +
            "    软链接文件：就像Windows中快捷方式一样，只是源文件的一个指向，删除软连接文件，源文件任存在。\n" +
            "    硬链接文件：比如当前目录下有2个文件，这2个文件除了名字不一样其他的一模一样，但是占用的实际磁盘空间还是只有1M，改变任何一个文件的内容另一个文件也会跟着改变；\n" +
            "```\n" +
            "\n" +
            "### 3.1.3 mkdir 命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： mkdir 目录名\n" +
            "#作用: 创建一个目录\n" +
            "# 示例: \n" +
            "    # mkdir test     创建一个test的目录。\n" +
            "    # mkdir -p test  若存在test，则不创建；若不存在，则创建\n" +
            "    # mkidr -p test/a/b  创建test目录，其下再创建a目录，a目录再创建b目录 。\n" +
            "```\n" +
            "\n" +
            "### 3.1.4 rm命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： rm [-rf] 文件|目录\n" +
            "#作用: 删除文件或目录\n" +
            "# 示例: \n" +
            "    # rm a.txt     删除a.txt,删除前询问 。\n" +
            "    # rm -f a.txt  直接删除a.txt ,不在询问 。\n" +
            "    # rm -r test  删除test目录，删除前询问\n" +
            "    # rm -rf test  直接删除test目录，不在询问 。\n" +
            "# 备注：任何的删除操作都是危险的动作，慎用 。\n" +
            "```\n" +
            "\n" +
            "### 3.1.5 mv命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： mv 源文件|目录 目标文件|目标目录\n" +
            "#作用: 有两层意思，分别为：\n" +
            "    1. 进行重命名文件或目录\n" +
            "    2. 进行移动文件或目录到目的目录 。\n" +
            "    \n" +
            "# 示例: \n" +
            "    # mv a.txt b.txt    修改文件名a.txt为b.txt 。\n" +
            "    # mv a.txt test/    移动a.txt 到test目录下\n" +
            "    # mv abc bcd        重命名目录abc为bcd .\n" +
            "    # mv abc bcd/       移动abc目录到bcd下 。\n" +
            "```\n" +
            "\n" +
            "### 3.1.6 cp命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： cp [-rf] 源文件|目录 目标文件|目录\n" +
            "#作用: 拷贝文件或目录为另一个文件或目录 。\n" +
            "    \n" +
            "# 示例: \n" +
            "    # cp a.txt b.txt   拷贝a.txt为b.txt ,若b.txt以存在，则提示是否继续拷贝 。\n" +
            "    # cp -f a.txt b.txt 拷贝a.txt为b.txt ,即使b.txt以前就存在，也是直接覆盖 。\n" +
            "    # cp -r abc bcd 拷贝abc目录为bcd ,若abc存在，则提示是否继续拷贝 。\n" +
            "    # cp -rf abc bcd 拷贝abc目录为bcd ,即使abc存在，则也是直接覆盖 。\n" +
            "```\n" +
            "\n" +
            "## 3.2 文件压缩与解压缩\n" +
            "\n" +
            "### 3.2.1 zipinfo命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： zipinfo zip文件\n" +
            "#作用:  查看zip文件里的信息。\n" +
            "    \n" +
            "# 示例: \n" +
            "    # zipinfo  abc.zip  查看abc.zip里的文件信息 。\n" +
            "    # zipinfo -v abc.zip 显示abc.zip里的每个文件的信息 。\n" +
            "```\n" +
            "\n" +
            "### 3.2.2 zip命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： zip 压缩文件 文件|目录\n" +
            "#作用:  将目标文件或目录进行压缩。\n" +
            "    \n" +
            "# 示例: \n" +
            "    # zip a.zip a.txt  将a.txt进行压缩形成a.zip 。\n" +
            "    # zip a.zip test/ 将test目录下的所有文件和目录压缩到a.zip 。\n" +
            "```\n" +
            "\n" +
            "### 3.2.3. gzip命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： gzip [-d] 文件|目录\n" +
            "#作用:  压缩|解压缩文件或目录\n" +
            "    \n" +
            "# 示例: \n" +
            "    # gzip a.txt  将a.txt压缩为a.txt.gz ,注意压缩后源文件已不存在。\n" +
            "    # gzip -d a.txt.gz 解压a.txt.gz文件\n" +
            "```\n" +
            "\n" +
            "### 3.2.4 unzip命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： unzip  文件\n" +
            "#作用:  解压缩文件\n" +
            "    \n" +
            "# 示例: \n" +
            "    # unzip  a.zip       解压文件\n" +
            "    # gzip -d a.txt.gz 解压a.txt.g3.2.5 gunzip命令\n" +
            "```\n" +
            "\n" +
            "### 3.2.5 gunzip命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： gunzip 压缩文件\n" +
            "#作用:  解压压缩文件\n" +
            "    \n" +
            "# 示例: \n" +
            "    # gunzip a.txt.gz  解压a.txt.gz\n" +
            "    # guzip  test.tar.gz  解压test.tar.gz\n" +
            "```\n" +
            "\n" +
            "### 3.2.6 tar命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： tar [-c|xzvf] 文件|压缩文件\n" +
            "#作用:  进行归档并创建压缩文件 或 进行解压归档压缩文档  \n" +
            "    \n" +
            "# 示例: \n" +
            "    # tar -cvzf a.tar  a.txt    # 将文件a.txt进行压缩并归档\n" +
            "    # tar -xvzf a.tar .     解压a.tar文件到当前目录 。\n" +
            "```\n" +
            "\n" +
            "## 3.3 文件传输\n" +
            "\n" +
            "### 3.3.1 tftp命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： tftp 远程主机\n" +
            "#作用:  连接远程主机，上传或下载文件\n" +
            "    \n" +
            "# 示例: (需连接到远程主机)\n" +
            "    # get a.txt        下载a.txt文件\n" +
            "    # put a.txt        上传a.txt到远程主机\n" +
            "```\n" +
            "\n" +
            "### 3.3.2 curl命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： curl url\n" +
            "#作用:  进行文件下载或者请求http协议数据\n" +
            "    \n" +
            "# 示例: \n" +
            "    # curl  http://www.baidu.com     #请求百度\n" +
            "    # curl -o baidu.html http://www.baidu.com    将请求到的数据保存到baidu.html中 。\n" +
            "```\n" +
            "\n" +
            "### 3.3.3 scp命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： scp 远程主机账号@远程IP地址 本地目录\n" +
            "#作用:  登录远程主机进行拷贝文件或目录\n" +
            "    \n" +
            "# 示例: \n" +
            "    # scp root@192.168.12.11:/soft/test.tar.gz /tools/    将远程主机目录下的/soft/test.tar.gz 拷贝到本地目录下的tools/下\n" +
            "    # scp root@192.168.12.11:/soft/ /tools/  将远程主机目录soft 拷贝到本地目录的tools/下 。\n" +
            "```\n" +
            "\n" +
            "### 3.3.4 rcp命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： scp 主机1 主机2\n" +
            "#作用:  远程主机间的文件或目录相互拷贝\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  rcp test 192.168.128.169:/test    拷贝当前目录下的test 到192.168.128.169的/test目录下\n" +
            "    #  rcp root@192.168.128.169:./test  /test 复制远程目录到本地的/test下\n" +
            "```\n" +
            "\n" +
            "## 3.4 文件属性查看\n" +
            "\n" +
            "### 3.4.1 file命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： file 文件名\n" +
            "#作用:  查看文件的类型\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  file a.txt   #查看a.txt是什么类型 \n" +
            "    #  file abc     #查看abc是什么类型 \n" +
            "```\n" +
            "\n" +
            "### 3.4.2 du命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： du 文件名\n" +
            "#作用:  查看文件的大小\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  du a.txt    #查看a.txt的文件大小，以k为单位\n" +
            "    #  du -h a.txt      #查看a.txt的文件大小，以M为单位 。\n" +
            "```\n" +
            "\n" +
            "## 3.5 文件目录权限设置\n" +
            "\n" +
            "### 3.5.1 chmod命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： 有两种用法\n" +
            "    chmod [u/g/o/a][+/-/=] rwx 文件/目录     +:增加权限，-取消权限， =设定权限\n" +
            "    chmod 数字 文件/目录\n" +
            "    \n" +
            "#作用:  为文件或目录设置权限。\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  chmod a=rw a.txt  为所有者设置读写权限 。\n" +
            "    #  chmod 644 a.txt   为所有者设置读权限，为用户设置写权限。\n" +
            "```\n" +
            "\n" +
            "# 4.文本内容查看及过滤\n" +
            "\n" +
            "## 4.1 文本内容查看\n" +
            "\n" +
            "### 4.1.1 cat命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： cat 文件名\n" +
            "#作用:  查看文本内容，会将内容全部显示。\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  cat a.txt  显示a.txt里的内容。\n" +
            "```\n" +
            "\n" +
            "### 4.1.2 more命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： more 文件名\n" +
            "#作用:  百分比显示文件内容，按Enter继续。\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  more a.txt  若只有一页，则全部显示，否则按百分比显示。\n" +
            "```\n" +
            "\n" +
            "### 4.1.3 tail命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式：\n" +
            "        tail 文件名        #查看文本内容\n" +
            "        tail -n 数量 文件名      #只显示倒数的几行\n" +
            "        tail -f 文件名         # 实时的查看文件写入的信息\n" +
            "#作用:  查看文本内容，\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  tail a.txt   查看文件内容，和cat效果一样 。\n" +
            "    #  tail -n 2 a.txt  显示a.txt最后两行 \n" +
            "    #  tail -f a.txt    实时监控a.txt文本内容。\n" +
            "```\n" +
            "\n" +
            "### 4.1.4 head命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： \n" +
            "    head 文件名\n" +
            "    head -n 数量 文件名\n" +
            "#作用:  查看文本内容。\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  head a.txt    查看文本内容，和cat效果一样。\n" +
            "    #  head -n 2 a.txt  查看文本的前两行 。\n" +
            "```\n" +
            "\n" +
            "## 4.2 文本内容筛选过滤\n" +
            "\n" +
            "### 4.3.1 grep命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： grep [选项] [模式] 文件\n" +
            "#作用:  文本搜索工具。\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  grep \"aaa\" a.txt    从a.txt中搜索aaa字符的行\n" +
            "    #  grep -v \"aaa\" a.txt  从a.txt中不包含aaa的行 \n" +
            "    #  grep -n \"aaa\" a.txt  从a.txt中搜索aaa字符的行,并在前面加上行号\n" +
            "    #  grep -i \"aaa\" a.txt  从a.txt中搜索aaa字符的行,其中忽略aaa的大小写\n" +
            "    #  grep -e \"a*\" a.txt   从a.txt中搜索匹配a字符的行\n" +
            "    #  ps -ef |grep \"mysql\"   查看mysql的进程\n" +
            "    \n" +
            "#备注：grep命令非常强大，详细用法请参考本人另一篇关于grep命令的博文\n" +
            "```\n" +
            "\n" +
            "### 4.3.2 sed命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： sed [选项]  文件\n" +
            "#作用:  文本编辑工具。\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  sed -n '2p' a.txt    从a.txt中的第二行内容\n" +
            "    #  sed '3,5d' a.txt     删除a.txt中第3到5行的内容，(注：源文件内容不变，只是回显内容会删除)\n" +
            "    #  sed '/aaa/d' a.txt   删除匹配aaa的行，从a.txt中 。\n" +
            "    \n" +
            "#备注：sed命令非常强大，详细用法请参考本人另一篇关于sed命令的博文\n" +
            "```\n" +
            "\n" +
            "### 4.3.3 awk命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： awk [选项]  文件\n" +
            "#作用:  文本分析工具。\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  awk '{print $5}' a.txt    显示a.txt中第5列的内容\n" +
            "    #  awk 'NR <=2 {print $1,$3,$5}' a.txt   显示前两行内容，每行只显示第1,3,5列 。\n" +
            "    #  awk '/^d/ {print $1,$9}' a.txt 显示以d开头的行，每行只显示第一，九列 。\n" +
            "    \n" +
            "#备注：awk命令非常强大，详细用法请参考本人另一篇关于awk命令的博文\n" +
            "```\n" +
            "\n" +
            "### 4.3.4 cut命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： cut 选项 文件\n" +
            "#作用:  用于剪切字符。\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  cut -c 1-3 a.txt 只输出每行第一到第三的字符\n" +
            "    #  cut -f4 -d\" \" a.txt 显示第四列，以空格为分隔符 \n" +
            "```\n" +
            "\n" +
            "### 4.3.5 col命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： col 选项 文件\n" +
            "#作用:  用于过滤字符。\n" +
            "    \n" +
            "# 示例: \n" +
            "    #  man ls | col-b > ls_help  过滤掉ls手册中的控制字符并输出到文件\n" +
            "```\n" +
            "\n" +
            "## 4.3 文本编辑\n" +
            "\n" +
            "## 4.3.1 vi/vim命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： vi/vim  文件\n" +
            "#作用:  用于编辑文件。\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  vi a.txt   编辑a.txt,可以进行修改里面的内容\n" +
            "    #  vim a.txt  编辑a.txt,可以进行修改里面的内容\n" +
            "# 备注：\n" +
            "vi和vim的用法基本一样，可以说vim是vi的增加版，就像记事本与notepad++\n" +
            "```\n" +
            "\n" +
            "## 4.4 输出到文本文件\n" +
            "\n" +
            "### 4.4.1 >命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： >  文件\n" +
            "#作用:  将内容输出到文件，若文件中有内容则覆盖。若文件不存在，则创建文件\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  ll > a.txt   查看详细后输出到a.txt 。\n" +
            "    #  cat a.txt > b.txt  将a.txt中的内容添加到b.txt中 。\n" +
            "```\n" +
            "\n" +
            "### 4.4.2 >>命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： >>  文件\n" +
            "#作用:  将内容追加到文件，若文件中有内容则追加。若文件不存在，则创建文件\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  ll >> a.txt   查看详细后追加到a.txt 。\n" +
            "    #  cat a.txt >> b.txt  将a.txt中的内容添加到b.txt中 。\n" +
            "```\n" +
            "\n" +
            "### 4.4.3 tee命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： tee  文件\n" +
            "#作用:  将内容输出到文件并输出内容显示在控制台上。若文件不存在，则创建文件，一般需要和管道符(|)一起使用。\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  cat a.txt | tee b.txt  将a.txt中的内容添加到b.txt中，同时将添加内容回显到控制台上 。\n" +
            "```\n" +
            "\n" +
            "## 4.5 文本内容处理\n" +
            "\n" +
            "### 4.5.1 join命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： join  文件1 文件2\n" +
            "#作用:  用于将两个文件中，指定栏目内容相同的行连接起来\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  join a.txt b.txt   若第一行相同，将后面的内容连接起来 。\n" +
            "```\n" +
            "\n" +
            "### 4.5.2 split命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： split 数量 文件\n" +
            "#作用:  用于将一个文件分割成数个\n" +
            "    \n" +
            "# 示例:  \n" +
            "    # split -5 c.txt  按每5行显示分隔出一个文件。\n" +
            "```\n" +
            "\n" +
            "### 4.5.3 uniq命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： uniq  文件\n" +
            "#作用:  用于检查及删除文本文件中重复出现的行列，注意：重复的行一定是相邻的行，若不相邻不会删除\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  uniq d.txt  将d.txt中相邻重复的行去掉 。\n" +
            "    #  uniq d.txt | sort  将d.txt中相邻重复的行去掉并排序\n" +
            "# 备注： 此命令经常和sort命令结合使用，用于去重和排序。\n" +
            "```\n" +
            "\n" +
            "### 4.5.4 sort命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： sort  文件\n" +
            "#作用:  对文本内容进行排序\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  sort a.txt  将a.txt中的内容进行排序，默认为升序。\n" +
            "    # sort -r a.txt 将a.txt中的内容进行相反顺序排序\n" +
            "    #  uniq d.txt | sort -r 将d.txt中相邻重复的行去掉并倒序排序\n" +
            "```\n" +
            "\n" +
            "### 4.5.5 paste命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： paste  文件1 文件2 ...\n" +
            "#作用:  用于合并文件的列。\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  cat a.txt b.txt  将两个文件的列合并起来显示 。\n" +
            "```\n" +
            "\n" +
            "# 5.用户|组操作\n" +
            "\n" +
            "## 5.1 用户增删改\n" +
            "\n" +
            "### 5.1.1 useradd命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： useradd 新用户\n" +
            "#作用:  创建用户\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  useradd test    创建test用户\n" +
            "    #  useradd -d /home/test  test  创建test用户，并指定test用户的家目录为home/test\n" +
            "    #  useradd -u 666 test   为test用户指定uid为666\n" +
            "```\n" +
            "\n" +
            "### 5.1.2 adduser命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： adduser 新用户\n" +
            "#作用:  创建用户\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  adduser test    创建test用户\n" +
            "    #  adduser -d /home/test  test  创建test用户，并指定test用户的家目录为home/test\n" +
            "    #  adduser -u 666 test   为test用户指定uid为666\n" +
            "# 备注：useradd和adduser使用上一致，设置两个命令可能就是为了解决用户将字母记错输返。\n" +
            "```\n" +
            "\n" +
            "### 5.1.3 userdel命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： userdel 用户\n" +
            "#作用:  删除用户\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  userdel test   删除test用户\n" +
            "    #  userdel -r test  删除test用户及其家目录\n" +
            "```\n" +
            "\n" +
            "### 5.1.4 usermod命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： usermod 用户\n" +
            "#作用:  修改用户\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  usermod -l test1 test    将用户test修改为test1\n" +
            "    #  usermod -d /home/test00  test   将用户test的家目录修改为/home/test00\n" +
            "    #  usermod -L test      锁定test用户的密码\n" +
            "    #  usermod -U test      解锁test用户的密码\n" +
            "```\n" +
            "\n" +
            "## 5.2 用户设置密码\n" +
            "\n" +
            "### 5.2.1 passwd命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： passwd 用户\n" +
            "#作用:  修改用户密码，输入命令回车后会引到用户设置新密码 。\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  passwd test     修改用户密码\n" +
            "```\n" +
            "\n" +
            "## 5.3 组的增删改\n" +
            "\n" +
            "### 5.3.1 groupadd命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： groupadd 用户组\n" +
            "#作用:  添加用户组\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  groupadd  test   添加用户组为test\n" +
            "    #  groupadd -g 9999 test  为创建用户组test并设置gid为9999\n" +
            "```\n" +
            "\n" +
            "### 5.3.2 groupdel命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： groupdel 用户组\n" +
            "#作用:  删除用户组\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  groupdel test  删除用户组test\n" +
            "```\n" +
            "\n" +
            "### 5.3.3 groupmod\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： groupmod 用户组\n" +
            "#作用:  修改用户组\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  groupmod -n root test    更改test用户组为root\n" +
            "```\n" +
            "\n" +
            "## 5.4 文件设置用户权限\n" +
            "\n" +
            "### 5.4.1 chown命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： chown 文件|目录 用户|用户组\n" +
            "#作用:  更改文件目录的用户或用户组\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  chown root /test/a.txt  把a.txt的所有者设置为root\n" +
            "    #  chown root:root /test/a.txt   把a.txt的所有者设置为root,组设置为root\n" +
            "    #  chown -R test:test *    把当前目录下的所有文件都设置为test用户和test用户组\n" +
            "```\n" +
            "\n" +
            "## 5.5 切换用户\n" +
            "\n" +
            "### 5.5.1 su命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： su [-] 用户\n" +
            "#作用:  切换用户\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  su test  切换当前用户为test用户   \n" +
            "    #  su - test 切换当前用户为test用户\n" +
            "# 备注： 第一次切换时需要输入密码\n" +
            "```\n" +
            "\n" +
            "# 6.任务管理器\n" +
            "\n" +
            "## 6.1 进程\n" +
            "\n" +
            "## 6.1 ps命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： ps [参数]\n" +
            "#作用:  显示当前系统的进程状态\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  ps -ef   显示所有进程\n" +
            "    #  ps -aux   显示所有进程\n" +
            "    #  ps -ef | grep mysql  查看mysql进程\n" +
            "    #  ps -u root 显示root用户进程。\n" +
            "```\n" +
            "\n" +
            "## 6.2 kill 命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： kill [参数]\n" +
            "#作用:  杀掉系统中执行的程序(进程)\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  kill 319877   杀掉进程319877\n" +
            "    #  kill -9  319877  强制杀掉进程319877\n" +
            "```\n" +
            "\n" +
            "## 6.2 系统资源\n" +
            "\n" +
            "### 6.2.1 top命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： top [参数]\n" +
            "#作用:  显示系统中各个进程的资源占用情况\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #   top    查看系统各个进程的资源占用，比如CPU ，内存信息。  \n" +
            "    #   top -n 5  动态更新5次结束\n" +
            "    #   top -d 5  每隔5秒更新一次\n" +
            "```\n" +
            "\n" +
            "### 6.2.2 vmstat命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： vmstat [参数]\n" +
            "#作用:  显示虚拟内存状态\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #     vmstat    显示内存信息\n" +
            "    #     vmstat  -s   以列表形式显示内存\n" +
            "    #     vmstat 2  每隔2秒刷新一次\n" +
            "```\n" +
            "\n" +
            "### 6.2.3 free命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： free [参数]\n" +
            "#作用:  查看系统内存信息\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #   free   显示内存信息，默认以kb为单位  \n" +
            "    #   free -m   显示内存信息，以mb为单位\n" +
            "    #   free -g   显示内存信息，以gb为单位\n" +
            "```\n" +
            "\n" +
            "### 6.2.4 df命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式：  df [参数] 分区\n" +
            "#作用:  查看磁盘占用空间\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #     df    查看各分区在磁盘占用情况\n" +
            "    #     df -h   以比较容易阅读方式查看磁盘使用情况\n" +
            "    #     df /dev/shm   查看该挂载点下的使用情况\n" +
            "```\n" +
            "\n" +
            "### 6.2.5 fdisk命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： fdisk [参数]\n" +
            "#作用:  进行磁盘分区管理\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  fdisk -l  查看所有分区情况   \n" +
            "```\n" +
            "\n" +
            "### 6.2.6 netstat命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： netstat [参数]\n" +
            "#作用:  显示各种网络信息\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #   netstat    查看各网络信息 \n" +
            "    #   netstat -an | grep 3306   查看3306端口的使用情况\n" +
            "```\n" +
            "\n" +
            "## 6.3 服务\n" +
            "\n" +
            "### 6.3.1 service命令（RHEL6）\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： service [参数]\n" +
            "#作用:  服务管理\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #   service --status-all    查看所有服务的运行状态  \n" +
            "    #   service  mysql  start   启动mysql\n" +
            "    #   service  mysql  stop    停止mysql\n" +
            "    #   service  mysql  restart   重启mysql\n" +
            "```\n" +
            "\n" +
            "\n" +
            "\n" +
            "### 6.3.2 systemctl命令（RHEL7）\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： systemctl [选项] [服务]\n" +
            "#作用:  对服务进行管理，如启动/重启/停止/查看服务\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  systemctl status httpd.service   查看http服务状态\n" +
            "    #  systemctl start httpd.service    启动http服务\n" +
            "    #  systemctl stop  httpd.service    停止http服务\n" +
            "    #  systemctl restart httpd.service  重启http服务\n" +
            "    #  systemctl status firewalld   查看防火墙状态\n" +
            "    #  systemctl start firewalld   开启防火墙\n" +
            "    #  systemctl stop firewalld    关闭防火墙\n" +
            "```\n" +
            "\n" +
            "### 6.3.3 chkconfig命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： chkconfig [参数]\n" +
            "#作用:  更新（启动或停止）和查询系统服务的运行级信息\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #     chkconfig -list   显示所有运行级系统服务的运行状态信息（on或off）\n" +
            "    #     chkconfig –add httpd        增加httpd服务\n" +
            "    #     chkconfig –del httpd        删除httpd服务\n" +
            "```\n" +
            "\n" +
            "# 7.网络管理\n" +
            "\n" +
            "## 7.1 ifconfig命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式：ifconfig \n" +
            "#作用:  查看或设置网络设备\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  ifconfig   查看网络信息，比如IP地址\n" +
            "    #  ifconfig eth0 down    关闭eth0的网卡 \n" +
            "    #  ifconfig eth0 up      开启eth0的网卡\n" +
            "    #  ifconfig eth0 hw ether 00:AA:BB:CC:DD:EE   修改Mac地址\n" +
            "    #  ifconfig eth0 add 32ffe:3840:320:2007::2/64      为网卡配置IPV6地址\n" +
            "    #  ifconfig eth0 del 32ffe:3840:320:2007::2/64      删除网卡的IPV6地址\n" +
            "    # ifconfig eth0 192.168.128.169     修改ip地址为192.168.128.169 \n" +
            "    # ifconfig eth0 192.168.128.169 netmask 255.255.255.0    修改IP和子网掩码\n" +
            "    # ifconfig eth0 192.168.1.56 netmask 255.255.255.0 broadcast 192.168.1.255  修改ip，子网掩码及网关\n" +
            "```\n" +
            "\n" +
            "## 7.2 ping命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： ping IP地址\n" +
            "#作用:  确认是否和某主机的网络相同\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  ping 192.168.12.12  确认是否能连通到192.168.12.12\n" +
            "    #  ping www.baidu.com  确认是否能正常访问百度\n" +
            "    #  ping -c 4 www.baidu.com  只ping四次\n" +
            "    #  ping -c 4 -i 2 www.baidu.com  只ping四次，每次间隔2s\n" +
            "```\n" +
            "\n" +
            "## 7.3 firewall-cmd命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： firewall-cmd [参数]\n" +
            "#作用:  防火墙端口管理\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  firewall-cmd --state   查看当前防火墙的运行状态\n" +
            "    #  firewall-cmd --zone=public --list-ports   查看所有放行的端口\n" +
            "    #  firewall-cmd --reload   重新加载修改的配置\n" +
            "    #  firewall-cmd --query-port=8888/tcp   查询端口8888是否被开放7.3 systemctl命令\n" +
            "\n" +
            "#语法格式： systemctl [选项] [服务]\n" +
            "#作用:  对服务进行管理，如启动/重启/停止/查看服务\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  systemctl status httpd.service   查看http服务状态\n" +
            "    #  systemctl start httpd.service    启动http服务\n" +
            "    #  systemctl stop  httpd.service    停止http服务\n" +
            "    #  systemctl restart httpd.service  重启http服务\n" +
            "    #  systemctl status firewalld   查看防火墙状态\n" +
            "    #  systemctl start firewalld   开启防火墙\n" +
            "    #  systemctl stop firewalld    关闭防火墙\n" +
            "\n" +
            "7.4 firewall-cmd命令\n" +
            "\n" +
            "#语法格式： firewall-cmd [参数]\n" +
            "#作用:  防火墙端口管理\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  firewall-cmd --state   查看当前防火墙的运行状态\n" +
            "    #  firewall-cmd --zone=public --list-ports   查看所有放行的端口\n" +
            "    #  firewall-cmd --reload   重新加载修改的配置\n" +
            "    #  firewall-cmd --query-port=8888/tcp   查询端口8888是否被开放\n" +
            "    #  firewall-cmd --add-port=8888/tcp    开启8888端口通过防火墙\n" +
            "    #  firewall-cmd --permanent --remove-port=123/tcp   关闭123端口\n" +
            "\n" +
            "\n" +
            "    #  firewall-cmd --add-port=8888/tcp    开启8888端口通过防火墙\n" +
            "    #  firewall-cmd --permanent --remove-port=123/tcp   关闭123端口\n" +
            "```\n" +
            "\n" +
            "# 8.安装更新配置\n" +
            "\n" +
            "## 8.1 yum命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： yum [选项]\n" +
            "#作用:  rpm的软件包管理器\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  yum install mysql     安装mysql\n" +
            "    #  yum remove mysql      卸载mysql \n" +
            "    #  yum clean  mysql      清除缓存目录下的安装包\n" +
            "    #  yum install           全部安装\n" +
            "    #  yum update            全部更新\n" +
            "    #  yum update mysql      更新mysql\n" +
            "    #  yum info   mysql      显示mysql安装包信息\n" +
            "    #  yum list  mysql       显示mysql安装包信息\n" +
            "    #  yum list              显示所有已安装包和可安装包\n" +
            "```\n" +
            "\n" +
            "## 8.2 sh命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： sh  可执行文件\n" +
            "#作用:  运行可执行文件，一般都是shell脚本\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  sh a.sh      运行a.sh文件，\n" +
            "    #  sh -x a.sh   运行并调试a.sh脚本\n" +
            "```\n" +
            "\n" +
            "# 9.系统相关\n" +
            "\n" +
            "## 9.1 环境变量\n" +
            "\n" +
            "### 9.1.1 set命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： set [参数]\n" +
            "#作用:  显示当前shell的变量，包括当前用户的变量;\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  abcd=100\n" +
            "    #  set | grep abcd    显示abcd的变量值\n" +
            "```\n" +
            "\n" +
            "### 9.1.2 unset命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： unset [参数]\n" +
            "#作用:  删除shell变量的值\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  abcd=100\n" +
            "    #  unset abcd    删除abcd的变量值\n" +
            "```\n" +
            "\n" +
            "### 9.1.3 env命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： env [参数]\n" +
            "#作用:  设置或显示当前环境变量\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  env    显示当前环境变量\n" +
            "    #  env abcd=10    定义环境变量\n" +
            "    #  env -u  abcd   删除已经定义的环境变量abcd\n" +
            "```\n" +
            "\n" +
            "### 9.1.4 export命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： export [参数]\n" +
            "#作用:  设置或显示环境变量\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  export  显示当前环境变量\n" +
            "    #  export abcd=101  定义环境变量\n" +
            "```\n" +
            "\n" +
            "## 9.2 重启与关机\n" +
            "\n" +
            "### 9.2.1 shutdown命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： shutdown [参数]\n" +
            "#作用:  关闭或重启\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  shutdown -h now      立即关机\n" +
            "    #  shutdown -r now      立即重启\n" +
            "    #  shutdown -h 22:30    22:30关机\n" +
            "```\n" +
            "\n" +
            "### 9.2.2 reboot命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： reboot [参数]\n" +
            "#作用:  重启计算机\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  reboot  重启\n" +
            "```\n" +
            "\n" +
            "### 9.2.3 poweroff命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： poweroff [参数]\n" +
            "#作用:  关闭计算机\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  poweroff    关闭计算机及电源\n" +
            "```\n" +
            "\n" +
            "### 9.2.4 halt命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： halt \n" +
            "#作用:  关闭操作系统\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  halt      关闭系统\n" +
            "    #  halt -p   关闭计算机及电源，等同于poweroff\n" +
            "    #  halt -f   强制关机\n" +
            "```\n" +
            "\n" +
            "### 9.2.5 exit命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： exit\n" +
            "#作用:  退出当前执行的shell\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  exit   退出当前shell \n" +
            "```\n" +
            "\n" +
            "## 9.3 查看系统信息\n" +
            "\n" +
            "### 9.3.1 uname命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： uname [参数]\n" +
            "#作用:  显示系统相关信息\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  uname       显示当前系统\n" +
            "    #  uname -an   显示系统的详细信息\n" +
            "    #  uname -r    显示内核信息\n" +
            "    #  uname -i    显示当前架构\n" +
            "```\n" +
            "\n" +
            "### 9.3.2 date命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： date [参数]\n" +
            "#作用:  显示或设定时间\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  date    查看当前时间\n" +
            "    #  date -s \"2021-04-04 22:38:56\"   设置时间为2021-04-04 22:38:56\n" +
            "```\n" +
            "\n" +
            "### 9.3.3 last命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： last \n" +
            "#作用:  显示最近用户或终端的登录情况\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  last    显示最近用户的登录情况\n" +
            "```\n" +
            "\n" +
            "### 9.3.4 history命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： history [参数]\n" +
            "#作用:  查看历史输入命令\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  history   查看历史命令\n" +
            "    #  history  | grep \"sed\"    查看输入过sed命令\n" +
            "    #  history -5  查看最近的5条命令\n" +
            "```\n" +
            "\n" +
            "### 9.3.5 who命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： who [参数]\n" +
            "#作用:  查看当前登录用户信息\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  who    查看登录用户信息\n" +
            "    #  who -H  带标题显示 \n" +
            "    #  who -b  输出系统最近启动时间\n" +
            "```\n" +
            "\n" +
            "## 9.4 定时任务\n" +
            "\n" +
            "### 9.4.1 crontab命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式：crontab [参数] \n" +
            "#作用:  任务调度\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  crontab -l   查看当前计划任务\n" +
            "    #  crontab -e   创建计划任务，打开后，需要以按照如下格式编辑\n" +
            "    \n" +
            "#备注\n" +
            "\n" +
            "#设置格式如下：\n" +
            "minute(分)   hour(小时)   day(天)   month(月)   week(周)   command(命令)   \n" +
            "\n" +
            "# 设置范围：\n" +
            "minute   是从0到59之间的任何整数\n" +
            "hour     是从0到23之间的任何整数\n" +
            "day      是从1到31之间的任何整数\n" +
            "month    是从1到12之间的任何整数\n" +
            "week     是从0到7之间的任何整数，其中0或7代表星期日\n" +
            "command  要执行的命令，可以是系统命令，也可以是自己编写的脚本文件\n" +
            "若某列没有设置，则使用*代替 。\n" +
            "\n" +
            "# 举例：\n" +
            "* * 1 * *   tar -czvf bk.tar.gz /log_bakup       # 每天进行一次归档备份\n" +
            "```\n" +
            "\n" +
            "## 9.5 运行管理员权限\n" +
            "\n" +
            "### 9.5.1 sudo命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： sudo [命令]\n" +
            "#作用:  运行以管理员权限运行命令,一般是非root用户进行操作\n" +
            "    \n" +
            "# 示例:  (假设当前账号为test)\n" +
            "    #  sudo mkdir abc   创建abc目录 。\n" +
            "```\n" +
            "\n" +
            "## 9.6 其它\n" +
            "\n" +
            "### 9.6.1 clear命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： clear\n" +
            "#作用:  清屏操作，也可以使用快捷键Ctrl + L\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  clear  清屏\n" +
            "```\n" +
            "\n" +
            "### 9.6.2 echo命令\n" +
            "\n" +
            "```markdown\n" +
            "#语法格式： echo [变量]\n" +
            "#作用:  输出变量值\n" +
            "    \n" +
            "# 示例:  \n" +
            "    #  echo  $abc  输出变量abc的值，需要提前定义abc的值\n" +
            "    #  echo  `pwd`  显示当前路径\n" +
            "```\n" +
            "\n" +
            "----\n" +
            "\n" +
            "\n" +
            "\n";
}
