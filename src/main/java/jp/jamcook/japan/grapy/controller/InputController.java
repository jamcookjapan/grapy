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

        String[] index_array = new String[chart.getWeek().size()];

        int value_cnt = 0;
        int targt_cnt = 0;
        int x_mover = 34;
        String title_value = "";
        String title_targt = "";
        for(int i = 0; i<chart.getWeek().size(); i++) {
            if (i == 0) continue;
            if(!chart.getWeek().get(i).getValue().isEmpty()) value_cnt++;
            if(!chart.getWeek().get(i).getTarget().isEmpty()) targt_cnt++;
        }

        title_value = chart.getWeek().get(0).getValue();
        title_targt = chart.getWeek().get(0).getTarget();
        chart.getWeek().remove(0);

        int[] value_array = new int[value_cnt];
        int[] target_array = new int[targt_cnt];
        for(int i = 0; i < chart.getWeek().size(); i++) {
            index_array[i] = chart.getWeek().get(i).getIndex();
            if(!chart.getWeek().get(i).getValue().isEmpty()) value_array[i] = Integer.parseInt(chart.getWeek().get(i).getValue().replace(",", ""));
            if(!chart.getWeek().get(i).getTarget().isEmpty()) target_array[i] = Integer.parseInt(chart.getWeek().get(i).getTarget().replace(",", ""));
        }
        if(chart.getWeek().size() > 31) {
            int width_range = 1150 - 115;
            x_mover = width_range / chart.getWeek().size();
        }

        int min_num = 0;
        int max_num = 0;
        for(int i = 0; i<value_array.length; i++) {
            if(i == 0) {
                min_num = value_array[i];
                max_num = value_array[i];
            }
            for(int j = 0; j<target_array.length; j++) {
                if (min_num > value_array[i]) min_num = value_array[i];
                if (min_num > target_array[j]) min_num = target_array[j];
                if (max_num < value_array[i]) max_num = value_array[i];
                if (max_num < target_array[j]) max_num = target_array[j];    
            }
        }

        if(min_num == max_num) {
            min_num = min_num / 2;
            max_num = max_num * 2;
        }

        int[] tmp_grid_array = new int[12];
        int range_hight = (max_num - min_num) / 9;
        for(int i = 0; i<tmp_grid_array.length; i++) {
            if(i == 0) {
                tmp_grid_array[i] = min_num - range_hight;
            } else {
                tmp_grid_array[i] = tmp_grid_array[i - 1] + range_hight;
            }
        }
        int[] grid_array = new int[12];
        int grid_idx = 11;
        for(int i = 0; i<tmp_grid_array.length; i++) {
            grid_array[grid_idx - i] = tmp_grid_array[i];
        }

        BufferedImage img = new BufferedImage(1200, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1200, 400);

        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, 400);
        g.drawLine(0, 0, 1200, 0);
        g.drawLine(0, 399, 1200, 399);
        g.drawLine(1199, 0, 1199    , 399);

        int tmp_grid_idx = 50;
        for(int i = 0; i<grid_array.length; i++) {
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(grid_array[i]), 20, tmp_grid_idx + 5);
            g.setColor(Color.GRAY);
            g.drawLine(100, tmp_grid_idx, 1150, tmp_grid_idx );
            tmp_grid_idx += 26;
        }

        int x_start = 110;
        g.setColor(Color.BLACK);
        for(int i = 0; i < index_array.length; i++) {
            if(index_array[i] != null) g.drawString(index_array[i], x_start, 375);
            x_start += x_mover;
        }

        g.setColor(Color.BLUE);
        int x_start_pos = 110;
        int hig_pix = 76;
        int btm_pix = 300;
        int[] point_array_x = new int[value_array.length];
        int[] point_array_y = new int[value_array.length];

        for(int i = 0; i<value_array.length;i++) {
            if(min_num == value_array[i] ) {
                g.fillOval( x_start_pos -1 , btm_pix - 5, 11, 11);
                point_array_x[i] = x_start_pos;
                point_array_y[i] = btm_pix - 5;
            } else {
                int post = (max_num - min_num) / (btm_pix - hig_pix);
                int res  = ((min_num - value_array[i]) * -1 ) / post;
                g.fillOval( x_start_pos -1 , btm_pix - res - 5, 11, 11);
                point_array_x[i] = x_start_pos;
                point_array_y[i] = btm_pix - res - 5;
            }
            x_start_pos += x_mover;
        }

        g.setColor(Color.BLUE);
        BasicStroke bs = new BasicStroke(3);
        g.setStroke(bs);
        for(int i = 0; i<point_array_x.length; i++) {
            int after_x = point_array_x[i];
            int after_y = point_array_y[i];
            if(i == 0) continue;
            int befor_x = point_array_x[i-1];
            int befor_y = point_array_y[i-1];
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawLine(befor_x + 5, befor_y + 5, after_x + 5, after_y + 5);
        }

        g.setColor(Color.ORANGE);
        x_start_pos = 110;
        hig_pix = 76;
        btm_pix = 300;
        int[] target_array_x = new int[target_array.length];
        int[] target_array_y = new int[target_array.length];

        for(int i = 0; i<target_array.length;i++) {
            if(min_num == target_array[i] ) {
                g.fillOval( x_start_pos , btm_pix - 5, 11, 11);
                target_array_x[i] = x_start_pos;
                target_array_y[i] = btm_pix - 5;
            } else {
                int post = (max_num - min_num) / (btm_pix - hig_pix);
                int res  = ((min_num - target_array[i]) * -1 ) / post;
                g.fillOval( x_start_pos , btm_pix - res - 5, 11, 11);
                target_array_x[i] = x_start_pos;
                target_array_y[i] = btm_pix - res - 5;
            }
            x_start_pos += x_mover;
        }

        g.setColor(Color.ORANGE);
        for(int i = 0; i<target_array_x.length; i++) {
            int after_x = target_array_x[i];
            int after_y = target_array_y[i];
            if(i == 0) continue;
            int befor_x = target_array_x[i-1];
            int befor_y = target_array_y[i-1];
            g.drawLine(befor_x + 5, befor_y + 5, after_x + 5, after_y + 5);
        }

        // start to end
        g.setColor(Color.BLACK);
        Font change = new Font("Arial", Font.BOLD, 18);
        g.setFont(change);
        g.drawString(String.valueOf(target_array[target_array.length - 1]) , target_array_x[target_array_x.length - 1] + 15, target_array_y[target_array_y.length - 1] + 10);
        g.drawString(String.valueOf(value_array[value_array.length - 1]) , point_array_x[point_array_x.length - 1] + 15, point_array_y[point_array_y.length - 1] + 10);


        g.setColor(Color.BLACK);
        g.drawString(title_value, 50, 25);
        g.setColor(Color.BLUE);
        g.fillRect(15 , 15, 30, 5 );

        g.setColor(Color.BLACK);
        g.drawString(title_targt, 150, 25);
        g.setColor(Color.ORANGE);
        g.fillRect(110 , 15, 30, 5 );


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
