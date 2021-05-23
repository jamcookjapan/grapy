package jp.jamcook.japan.grapy.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import model.Chart;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Controller
public class InputController {

    @RequestMapping("/input")
    public String input() {
        return "input";
    }

    @RequestMapping("/createImage")
    public HttpEntity<byte[]> createimage(@RequestBody Chart chart) throws IOException {
        System.out.println(chart.getName());

        String[] index_array = new String[chart.getWeek().size()];
        int[] value_array = new int[chart.getWeek().size()];
        for(int i = 0; i < chart.getWeek().size(); i++) {
            index_array[i] = chart.getWeek().get(i).getIndex();
            value_array[i] = Integer.parseInt(chart.getWeek().get(i).getValue().replace(",", ""));
        }

        BufferedImage img = new BufferedImage(1200, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) img.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1200, 400);

        g.setColor(Color.BLACK);
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, 400);
        g.drawLine(0, 0, 1200, 0);
        g.drawLine(0, 399, 1200, 399);
        g.drawLine(1199, 0, 1199    , 399);

        g.drawString("11,000,000", 20, 40);
        g.drawString("10,950,000", 20, 70);
        g.drawString("10,800,000", 20, 100);
        g.drawString("10,650,000", 20, 130);
        g.drawString("10,500,000", 20, 160);
        g.drawString("10,350,000", 20, 190);
        g.drawString("10,200,000", 20, 220);
        g.drawString("10,050,000", 20, 250);
        g.drawString("9,900,000", 20, 280);
        g.drawString("9,750,000", 20, 310);

        int x_start = 80;
        for(int i = 1; i<=31; i++) {
            x_start += 34;
            g.drawString(String.valueOf(i), x_start, 350);
        }

        g.setColor(Color.GRAY);
        g.drawLine(100, 40, 1150, 40);
        g.drawLine(100, 70, 1150, 70);
        g.drawLine(100, 100, 1150, 100);
        g.drawLine(100, 130, 1150, 130);
        g.drawLine(100, 160, 1150, 160);
        g.drawLine(100, 190, 1150, 190);
        g.drawLine(100, 220, 1150, 220);
        g.drawLine(100, 250, 1150, 250);
        g.drawLine(100, 280, 1150, 280);
        g.drawLine(100, 310, 1150, 310);

        // 投資の数字を描写する
        g.setColor(Color.BLUE);
        int tradepoint[] = {10640000,10640000,10640000,10640000,10620000,10650000,10780000,10780000,10780000,10760000,10570000,10530000,9960000,10250000,10250000,10250000,10310000,10310000,10270000,10270000,10280000};
        // 数字をY座標のする
        // 11000000 = 0 , 9750000 = 270
        // 1250000 / 270 = 4630
        // 10640000 = 890000 = 192
        // 192 - 40 =

        // 11000000 から 描写したい実績を引き、差分を4260で割った数に40を足せば座標が出る。
        
        int x_start_trade = 120;
        int before_pointo = 0;
        BasicStroke bs1 = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        for(int i = 0; i<tradepoint.length; i++) {
            g.setStroke(bs1);
            int y_point = (11000000 - tradepoint[i]) / 4630 + 70;
            if(i == 0) {
                before_pointo = y_point;
                g.fillRect( (x_start_trade) - 5, y_point - 5, 10, 10);
                continue;
            }
            g.drawLine(x_start_trade, before_pointo, x_start_trade + 34, y_point);
            before_pointo = y_point;
            g.fillRect( (x_start_trade + 34) - 5, y_point - 5, 10, 10);
            x_start_trade += 34;
        }

        // int xx = 100;
        // int yy = 240;
        // int xx_move = 1;
        // for(int i = 0; i<100; i++) {
        //     xx += xx_move;
        //     yy -= xx_move;
        //     g.fillRoundRect(xx, yy, 5, 5, 5, 5);
        // }

        g.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedOutputStream os = new BufferedOutputStream(bos);
        img.flush();
        ImageIO.write( img, "png", os );
        byte[] b = bos.toByteArray();
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);		
        headers.setContentLength(b.length);
        return new HttpEntity<byte[]>(b, headers);
    }

    @RequestMapping("/get")
    public HttpEntity<byte[]> imageCreate() throws IOException {
        BufferedImage img = new BufferedImage(1200, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) img.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1200, 400);

        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, 400);
        g.drawLine(0, 0, 1200, 0);
        g.drawLine(0, 400, 1200, 400);
        g.drawLine(1200, 0, 1200, 400);


        g.drawString("11,000,000", 20, 40);
        g.drawString("10,950,000", 20, 70);
        g.drawString("10,800,000", 20, 100);
        g.drawString("10,650,000", 20, 130);
        g.drawString("10,500,000", 20, 160);
        g.drawString("10,350,000", 20, 190);
        g.drawString("10,200,000", 20, 220);
        g.drawString("10,050,000", 20, 250);
        g.drawString("9,900,000", 20, 280);
        g.drawString("9,750,000", 20, 310);

        int x_start = 80;
        for(int i = 1; i<=31; i++) {
            x_start += 34;
            g.drawString(String.valueOf(i), x_start, 350);
        }

        g.setColor(Color.GRAY);
        g.drawLine(100, 40, 1150, 40);
        g.drawLine(100, 70, 1150, 70);
        g.drawLine(100, 100, 1150, 100);
        g.drawLine(100, 130, 1150, 130);
        g.drawLine(100, 160, 1150, 160);
        g.drawLine(100, 190, 1150, 190);
        g.drawLine(100, 220, 1150, 220);
        g.drawLine(100, 250, 1150, 250);
        g.drawLine(100, 280, 1150, 280);
        g.drawLine(100, 310, 1150, 310);

        // 投資の数字を描写する
        g.setColor(Color.BLUE);
        int tradepoint[] = {10640000,10640000,10640000,10640000,10620000,10650000,10780000,10780000,10780000,10760000,10570000,10530000,9960000,10250000,10250000,10250000,10310000,10310000,10270000,10270000,10280000};
        // 数字をY座標のする
        // 11000000 = 0 , 9750000 = 270
        // 1250000 / 270 = 4630
        // 10640000 = 890000 = 192
        // 192 - 40 =

        // 11000000 から 描写したい実績を引き、差分を4260で割った数に40を足せば座標が出る。
        
        int x_start_trade = 120;
        int before_pointo = 0;
        BasicStroke bs1 = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        for(int i = 0; i<tradepoint.length; i++) {
            g.setStroke(bs1);
            int y_point = (11000000 - tradepoint[i]) / 4630 + 70;
            if(i == 0) {
                before_pointo = y_point;
                g.fillRect( (x_start_trade) - 5, y_point - 5, 10, 10);
                continue;
            }
            g.drawLine(x_start_trade, before_pointo, x_start_trade + 34, y_point);
            before_pointo = y_point;
            g.fillRect( (x_start_trade + 34) - 5, y_point - 5, 10, 10);
            x_start_trade += 34;
        }

        // int xx = 100;
        // int yy = 240;
        // int xx_move = 1;
        // for(int i = 0; i<100; i++) {
        //     xx += xx_move;
        //     yy -= xx_move;
        //     g.fillRoundRect(xx, yy, 5, 5, 5, 5);
        // }

        g.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedOutputStream os = new BufferedOutputStream(bos);
        img.flush();
        ImageIO.write( img, "png", os );
        byte[] b = bos.toByteArray();
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);		
        headers.setContentLength(b.length);
        return new HttpEntity<byte[]>(b, headers);
    }
}
