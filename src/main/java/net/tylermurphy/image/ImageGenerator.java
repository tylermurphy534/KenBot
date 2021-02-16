package net.tylermurphy.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import net.dv8tion.jda.api.entities.User;

public class ImageGenerator {

	public static BufferedImage GenerateLevelUpImage(int level, User u) {
		BufferedImage background,avatar,frame;
		try {
			background = ImageIO.read(new URL(
					"https://i.pinimg.com/originals/08/13/d9/0813d9eeaea3dddd22cde3272dcdec1e.jpg"));
			frame = ImageIO.read(new URL(
					"https://i.pinimg.com/originals/53/64/39/5364393b6764334c4b70e514a55ced72.png"));
			avatar = ImageIO.read(new URL(
					u.getAvatarUrl()));
					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
		return img;
	}
	
}
