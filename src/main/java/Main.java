import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by undancer on 15/8/25.
 */
public class Main {
    public static void main(String[] args) throws FileSystemException {
//        FileSystemManager fsManger = VFS.getManager();
//        FileObject dir = fsManger.resolveFile("oss://test");
//        System.out.println(dir);
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        FileSystemManager manager = VFS.getManager();
        FileObject object = manager.resolveFile("oss://boxfish/true.jpg");
        System.out.println(object);

//        applicationContext.close();


    }
}
