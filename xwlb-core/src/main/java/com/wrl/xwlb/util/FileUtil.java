package com.wrl.xwlb.util;

import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.common.exception.ExceptionType;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class FileUtil {

  public static void convertBase64ToImage(String base64Code, String fileName){
    BufferedImage image;
    byte[] imageByte;
    try {
      imageByte = DatatypeConverter.parseBase64Binary(base64Code);
      ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
      image = ImageIO.read(new ByteArrayInputStream(imageByte));
      bis.close();
      File outfile = new File(fileName);
      ImageIO.write(image, "png", outfile);
    } catch (IOException e) {
      throw new CommonException(ExceptionType.COMMON_SERVER_ERROR);
    }
  }
}
