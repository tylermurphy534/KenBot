package net.tylermurphy.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class ImageGenerator {

	public static byte[] GenerateLevelUpImage(int level, User u) {
		BufferedImage background,avatar,frame;
		try {
			background = ImageIO.read(new URL(
					"https://i.pinimg.com/originals/08/13/d9/0813d9eeaea3dddd22cde3272dcdec1e.jpg"));
			frame = ImageIO.read(new URL(
					"https://i.pinimg.com/originals/53/64/39/5364393b6764334c4b70e514a55ced72.png"));
			avatar = ImageIO.read(new URL(
					u.getAvatarUrl()));
			
			BufferedImage img = new BufferedImage(220, 100, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			g2d.drawImage(background, 0, 0, 300, 100, null);
			g2d.drawImage(avatar, 12, 12, 100-24, 100-24, null);
			g2d.drawImage(frame, 3, 3, 100-6, 100-6, null);
			g2d.setPaint(Color.white);
			g2d.setFont(new Font("Dialog", Font.BOLD, 20));
			String s = "LEVEL UP!";
			g2d.drawString(s, 105, 27);
			g2d.setFont(new Font("Dialog", Font.BOLD, 50));
			FontMetrics fm = g2d.getFontMetrics();
			int x = 225 - fm.stringWidth(level+"")/2 - 70;
			g2d.drawString(level+"", x, 80);

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
				ImageIO.read(new URL(
					"https://i.pinimg.com/originals/69/e9/3c/69e93ced914a9230dcf2b9ba160f129f.jpg")),
				300
			);
			avatar = makeRoundedCorner(
				ImageIO.read(new URL(u.getAvatarUrl())),
				100
			);
			
			BufferedImage img = new BufferedImage(500, 280, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			g2d.drawImage(background, 0, 0, 500, 280, null);
			g2d.drawImage(avatar, 500/2-150/2, 280/2-150/2-25, 150, 150, null);
			g2d.setPaint(Color.white);
			
			String s = String.format("%s#%s has joined the server", u.getName(), u.getDiscriminator());
			g2d.setFont(new Font("Dialog", Font.BOLD, 20));
			FontMetrics fm = g2d.getFontMetrics();
			int x = 500/2 - fm.stringWidth(s)/2;
			g2d.drawString(s, x, 235);
			
			s = String.format("Member #%s", g.getMemberCount());
			g2d.setFont(new Font("Dialog", Font.BOLD, 15));
			fm = g2d.getFontMetrics();
			x = 500/2 - fm.stringWidth(s)/2;
			g2d.drawString(s, x, 255);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write(img, "png", baos);
		    byte[] bytes = baos.toByteArray();
		    
		    return bytes;
					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
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
	
}
