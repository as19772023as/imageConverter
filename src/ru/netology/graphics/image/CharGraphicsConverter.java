package ru.netology.graphics.image;

import java.awt.*;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;


public class CharGraphicsConverter implements TextGraphicsConverter {
    protected int width;
    protected int height;
    protected double maxRatio;
    protected TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        BufferedImage img = ImageIO.read(new URL(url)); // скачать картинку из Инета
        ImageIO.write(img, "png", new File("out.png")); // можно отслеживать файл в проекте т.ж. в другх местах кода

        int newWidth = img.getWidth();
        int newHeight = img.getHeight();
        double proportion;

        if (maxRatio != 0.0) {
            double ratio = (double) img.getWidth() / (double) img.getHeight();
            if (ratio > maxRatio) {
                throw new BadImageSizeException(ratio, maxRatio);
            }
        }
        if ((width != 0 && img.getWidth() > width) && (height != 0 && img.getHeight() > height)) {
            proportion = Math.max((double) img.getWidth() / (double) width, (double) img.getHeight() / (double) height);
            newHeight = (int) (img.getHeight() / proportion);
            newWidth = (int) (img.getWidth() / proportion);
        } else {
            if (width != 0) {
                proportion = (double) img.getWidth() / (double) width;
                newWidth = (int) (img.getWidth() / proportion);
            } else {
                if (height != 0) {
                    proportion = (double) img.getHeight() / (double) height;
                    newHeight = (int) (img.getHeight() / proportion);
                }
            }
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH); // плавно сузиться до размеров
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY); // пустая черно-белая картинка
        Graphics2D graphics = bwImg.createGraphics(); // Попросим у этой картинки инструмент для рисования на ней:
        graphics.drawImage(scaledImage, 0, 0, null); //  копирует содержимое из нашей суженной картинки:
        WritableRaster bwRaster = bwImg.getRaster(); // проход по пикселям

        schema = new CharColorConverter();
        StringBuilder textImg = new StringBuilder();

        int count = 0;
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                textImg.append(c);
                textImg.append(c);

                count++;
            }
            if (count == newWidth) {
                //if (count == newWidth * 2) {
                textImg.append('\n');
                count = 0;
            }
        }
//        for (int i = newWidth * 2; i < textImg.length(); i += newWidth * 2 + 1) {
//            textImg.insert(i, '\n');
//        }

        return textImg.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}

