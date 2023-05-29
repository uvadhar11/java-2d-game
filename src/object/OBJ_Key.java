package object;

import javax.imageio.ImageIO;
import java.io.IOException;

// key object class extending the super object class
public class OBJ_Key extends SuperObject {
    public OBJ_Key() {
        this.name = "Key";
        try {
            // get the image and store it in image (a buffered image var)
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
        }
        catch (IOException e) {
            // print the error if there is one
            e.printStackTrace();
        }
    }
}
