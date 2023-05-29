package object;

import javax.imageio.ImageIO;
import java.io.IOException;

// boots object class extending the super object class
public class OBJ_Boots extends SuperObject {
    public OBJ_Boots() {
        name = "Boots";
        try {
            // get the image and store it in image (a buffered image var)
            image = ImageIO.read(getClass().getResourceAsStream("/objects/boots.png"));
        }
        catch (IOException e) {
            // print the error if there is one
            e.printStackTrace();
        }
    }
}
