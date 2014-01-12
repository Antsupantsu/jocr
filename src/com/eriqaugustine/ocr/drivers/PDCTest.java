package com.eriqaugustine.ocr.drivers;

import com.eriqaugustine.ocr.image.CharacterImage;
import com.eriqaugustine.ocr.image.TextImage;
import com.eriqaugustine.ocr.pdc.PDCClassifier;

import com.eriqaugustine.ocr.utils.CharacterUtils;
import com.eriqaugustine.ocr.utils.ImageUtils;

import com.eriqaugustine.ocr.utils.Props;

import magick.ImageInfo;
import magick.MagickImage;

/**
 * A test to see how PDC is doing on a simple test set.
 */
public class PDCTest {
   public static void main(String[] args) throws Exception {
      pdcTest();
   }

   public static void pdcTest() throws Exception {
      String alphabet = Props.getString("HIRAGANA");

      PDCClassifier classy = new PDCClassifier(CharacterImage.generateFontImages(alphabet),
                                               //  alphabet, false, 1);
                                               alphabet, true, 1,
                                               new String[]{CharacterUtils.DEFAULT_FONT_FAMILY});

      // Not exactly hiragana.
      String characters = "あいうえおかきくけこさしすせそたちつてとなにぬねの" +
                          "はひふへほまみむめもやわゆんよらりるれろ";

      ImageInfo info = new ImageInfo("testImages/partHiragana.png");
      MagickImage baseImage = new MagickImage(info);

      int count = 0;
      int hits = 0;

      MagickImage[][] gridTextImages = TextImage.gridBreakup(baseImage);
      for (int row = 0; row < gridTextImages.length; row++) {
         for (int col = 0; col < gridTextImages[row].length; col++) {
            MagickImage gridTextImage = ImageUtils.shrinkImage(gridTextImages[row][col]);

            // System.out.println(ImageUtils.asciiImage(gridTextImage) + "\n-\n");

            String prediction = classy.classify(gridTextImage);
            System.out.println(String.format("Classify (%d, %d)[%s]: %s",
                                             row, col,
                                             "" + characters.charAt(count),
                                             prediction));

            if (prediction.equals("" + characters.charAt(count))) {
               hits++;
            }

            count++;
         }
      }

      System.err.println("Hits: " + hits + " / " + count + " (" + ((double)hits / count) + ")");
   }
}
