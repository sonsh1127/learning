import java.util.ArrayList;
import java.util.List;

public class ImageWrite {

    public static void main(String[] args) throws Exception{
        /*BufferedImage image1 = ImageIO.read(new File("game.png"));
        BufferedImage image2 = ImageIO.read(new File("ryan.jpg"));

        int width = Math.max(image1.getWidth(), image2.getWidth());
        int height = image1.getHeight() + image2.getHeight();

        BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();

        graphics.setBackground(Color.WHITE);
        graphics.drawImage(image1, 0, 0, null);
        graphics.drawImage(image2, 0, image1.getHeight(), null);
        ImageIO.write(mergedImage, "png", new File("merged.gif"));*/



        List<CommonBean> beans = new ArrayList<>();
        beans.add(new ConcreteBean());
        MyBeanFactory factory = new MyBeanFactory(beans);
        factory.init();
    }
}

class CommonBean {

    public void start() {
        preStart();
        System.out.println("start");
    }

    // the hooking method
    public void preStart() {

    }
}

class MyBeanFactory {

    public MyBeanFactory(List<CommonBean> beans) {
        this.beans = beans;
    }

    private List<CommonBean> beans;
    public void init() {
        beans.stream().forEach(bean -> {
            bean.start();
        });
    }
}

class ConcreteBean extends CommonBean {

    public void preStart() {
        System.out.println("Init before start");
    }
}
