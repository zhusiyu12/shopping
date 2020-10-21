import org.junit.Test;

import java.io.FileInputStream;

import java.io.InputStream;
import com.edu.util.FtpUtil;

public class FtpTest {

    @Test
    public void test() throws Exception {
        InputStream local = new FileInputStream("E:\\pic\\1.jpg");
        FtpUtil.uploadFile("192.168.43.49",21,"ftpuser","ftpuser",
                "/home/ftpuser/www/images",
                "/2020/10/21","hello2.jpg",local);


    }
}
