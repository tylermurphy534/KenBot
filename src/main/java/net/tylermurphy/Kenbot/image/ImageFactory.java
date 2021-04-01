package net.tylermurphy.Kenbot.image;

import java.awt.AlphaComposite;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class ImageFactory {

	public static byte[] GenerateLevelUpImage(int level, User u) {
		BufferedImage background,avatar,frame;
		try {
			background = getImageFromURL("https://i.pinimg.com/originals/69/e9/3c/69e93ced914a9230dcf2b9ba160f129f.jpg");
			frame = getImageFromURL("https://i.pinimg.com/originals/53/64/39/5364393b6764334c4b70e514a55ced72.png");
			avatar = getImageFromURL(u.getAvatarUrl());
			
			BufferedImage img = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			g2d.drawImage(background, 0, 0, 1200, 400, null);
			g2d.drawImage(avatar, 48, 48, 400-96, 400-96, null);
			g2d.drawImage(frame, 12, 12, 400-24, 400-24, null);
			g2d.setPaint(Color.white);
			g2d.setFont(new Font("Dialog", Font.BOLD, 80));
			String s = "LEVEL UP!";
			g2d.drawString(s, 475, 110);
			g2d.setFont(new Font("Dialog", Font.BOLD, 200));
			FontMetrics fm = g2d.getFontMetrics();
			int x = (1000+350)/2 - fm.stringWidth(level+"")/2;
			g2d.drawString(level+"", x, 320);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write(img, "png", baos);
		    byte[] bytes = baos.toByteArray();
		    
		    return bytes;
					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static byte[] WelcomeImage(User u, Guild g) {
		BufferedImage background,avatar;
		try {
			background = makeRoundedCorner(
					getImageFromURL("https://i.pinimg.com/originals/69/e9/3c/69e93ced914a9230dcf2b9ba160f129f.jpg"),
				300
			);
			avatar = makeRoundedCorner(
					getImageFromURL(u.getAvatarUrl()),
				100
			);
			
			BufferedImage img = new BufferedImage(1000, 560, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			g2d.drawImage(background, 0, 0, 1000, 560, null);
			g2d.drawImage(avatar, 1000/2-300/2, 560/2-300/2-25, 300, 300, null);
			g2d.setPaint(Color.white);
			
			String s = String.format("%s#%s has joined the server", u.getName(), u.getDiscriminator());
			g2d.setFont(new Font("Dialog", Font.BOLD, 40));
			FontMetrics fm = g2d.getFontMetrics();
			int x = 1000/2 - fm.stringWidth(s)/2;
			g2d.drawString(s, x, 470);
			
			s = String.format("Member #%s", g.getMemberCount());
			g2d.setFont(new Font("Dialog", Font.BOLD, 30));
			fm = g2d.getFontMetrics();
			x = 1000/2 - fm.stringWidth(s)/2;
			g2d.drawString(s, x, 510);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write(img, "png", baos);
		    byte[] bytes = baos.toByteArray();
		    
		    return bytes;
					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static BufferedImage GenerateEjectFrame(String message, int frame, float totalFrames, BufferedImage space1, BufferedImage avatar) {
		try {
			int w = 600;
			int h = 400;
			BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g2d = img.createGraphics();
			g2d.drawImage(space1, (int) (-100+100/totalFrames*frame), 0, (int)(w*1.5), (int)(h*1.5), null);
			
			BufferedImage ravater = rotate(avatar, 180/24*frame);
			g2d.drawImage(ravater, (w+2*avatar.getWidth()+100)/24*frame-avatar.getWidth()-50, h/2-avatar.getHeight()/2, 150, 150,  null);
			
			if(frame > 12) {
				int length = (message.length() * (frame-11)/12);
				message = message.substring(0, length);
				
				g2d.setFont(new Font("Dialog", Font.BOLD, 25));
				FontMetrics fm = g2d.getFontMetrics();
				int x = w/2 - fm.stringWidth(message)/2;
				int y = h/2;
				g2d.drawString(message, x, y);
			}
			
			return img;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage rotate(BufferedImage bimg, double angle) {

	    int w = bimg.getWidth()+50;    
	    int h = bimg.getHeight()+50;

	    BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);  
	    Graphics2D graphic = rotated.createGraphics();
	    graphic.rotate(Math.toRadians(angle), w/2, h/2);
	    graphic.drawImage(bimg, null, w/2-bimg.getWidth()/2, h/2-bimg.getHeight()/2);
	    graphic.dispose();
	    return rotated;
	}
	
	private static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
	    int w = image.getWidth();
	    int h = image.getHeight();
	    BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2 = output.createGraphics();

	    g2.setComposite(AlphaComposite.Src);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setColor(Color.WHITE);
	    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

	    g2.setComposite(AlphaComposite.SrcAtop);
	    g2.drawImage(image, 0, 0, null);
	    
	    g2.dispose();
	    
	    return output;
	}
	
	public static BufferedImage getImageFromURL(String surl) {
		try {
			final URL url = new URL(surl);
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			final BufferedImage image = ImageIO.read(connection.getInputStream());
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
