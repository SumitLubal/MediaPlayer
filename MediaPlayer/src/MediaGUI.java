
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;

import java.awt.Canvas;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.logger.Logger;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.DefaultMediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

//import javax.swing.plaf.nimbus.NimbusLookAndFeel

public class MediaGUI extends JFrame implements ActionListener
{
	File newFile;
	String song1;
	 String song;

	JMenuBar mb;
    JMenu file,play,help;
    JMenuItem open,gesture,history,exit;
    JSlider js;
	JButton pre,ply,pause,next,forward,backward;
	JButton open1;
	JButton volUp,volDown,webcam;
	MediaPlayerFactory mediaPlayerFactory;
    EmbeddedMediaPlayerComponent mediaPlayer;
    String mediaPath;
    JPanel viewMedia=new JPanel();
    int key1,ctrl1;
    File dir;
    File mp3files[];
    int mp3Count=0,flg=0;
    long count=0;
    static String vlcPath="C:\\Program Files (x86)\\VideoLAN\\VLC";
    JPanel panel2;
    
    MediaGUI()
    {
    	super("Media Player(Gesture Control)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1370,727);
        setResizable(true);
        setLocation(0,0);
      
        panel2 = new JPanel();
       
     //   panel2.setBackground(Color.BLACK);
        panel2.setLayout(null);
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
     //   panel2.setSize(200,100);
      
        
        ImageIcon pr = new ImageIcon("21.png");
        ImageIcon pl = new ImageIcon("24.png");
        ImageIcon pu= new ImageIcon("23.png");
        ImageIcon nx= new ImageIcon("22.png");
        ImageIcon op= new ImageIcon("open.png");
        
        open1=new JButton(op);
        open1.setActionCommand("Open");
        open1.setSize(40,40);
        open1.addActionListener(this);
       
        js=new JSlider();
        
    //    js.setBounds(0,0,600,20);
        js.setMajorTickSpacing(1);
        js.setMinorTickSpacing(1);
        js.setPaintTicks(true);
       // js.setPaintLabels(true);
        js.setMinimum(0);
        js.setValue(0);
        js.setValueIsAdjusting(true); 
      //  js.addChangeListener( (ChangeListener) this );
        
        pre=new JButton(pr);
        pre.setActionCommand("pre");
        ply=new JButton(pl);
        ply.setActionCommand("play");
        pause=new JButton(pu);
        pause.setActionCommand("pause");
        next=new JButton(nx);
        next.setActionCommand("next");
        forward=new JButton("Forward");
        forward.setActionCommand("Forward");
        backward=new JButton("Backward");
        backward.setActionCommand("Backward");
        webcam=new JButton("GestureOpen");
        webcam.setActionCommand("GestureOpen");
        
        
        volUp=new JButton("Up");
        volUp.setActionCommand("up");
        
        volDown=new JButton("Down");
        volDown.setActionCommand("down");
        pre.addActionListener(this);
        ply.addActionListener(this);
        pause.addActionListener(this);
        next.addActionListener(this);
        forward.addActionListener(this);
        backward.addActionListener(this);
        volUp.addActionListener(this);
        volDown.addActionListener(this);
	    webcam.addActionListener(this);


        	//DefaultEmbeddedMediaPlayer(libvlc,instance);
        	
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),vlcPath);
    	mediaPlayer=new EmbeddedMediaPlayerComponent();
    //  DefaultMediaPlayer defmp=new DefaultEmbeddedMediaPlayer(libvlc,instance);
        this.setContentPane(mediaPlayer);
        mediaPlayer.setCursorEnabled(true);
        mediaPlayer.getMediaPlayer().setFullScreen(true);
    	
        panel2.add(open1);
        panel2.add(pre);
        panel2.add(ply);
        panel2.add(pause);
        panel2.add(next);
        panel2.add(js);
        panel2.add(forward);
        panel2.add(backward);
        panel2.add(volUp);
        panel2.add(volDown);
		panel2.add(webcam);
        this.add(panel2,BorderLayout.SOUTH);
        
        file=new JMenu("File");
        file.setMnemonic('F');
        file.addSeparator();
        key1=KeyEvent.VK_O;
        ctrl1=InputEvent.CTRL_MASK;
        file.add(makeMenuItem("Open",key1,ctrl1));
        key1=KeyEvent.VK_G;
        ctrl1=InputEvent.CTRL_MASK;
        file.add(makeMenuItem("Open Gesture",key1,ctrl1));
        key1=KeyEvent.VK_R;
        ctrl1=InputEvent.CTRL_MASK;
        file.add(makeMenuItem("Recent Played",key1,ctrl1));
        key1=KeyEvent.VK_X;
        ctrl1=InputEvent.CTRL_MASK;
        file.add(makeMenuItem("Exit...",key1,ctrl1));
        
        play = new JMenu("Play");
        play.setMnemonic('P');
        play.addSeparator();
        help= new JMenu("Help");
        help.setMnemonic('H');
        help.addSeparator();
        help.add(makeMenuItem("About",key1,ctrl1));
        
        
        mb= new JMenuBar();
        mb.add(file);
        mb.add(play);
        mb.add(help);
       // setJMenuBar(mb);
          	
    }
        
    private JMenuItem makeMenuItem(String name,int key,int ctrl)
    {
        JMenuItem m = new JMenuItem(name);
        KeyStroke ctrlH = KeyStroke.getKeyStroke(key,ctrl);
        m.setAccelerator(ctrlH);
        m.addActionListener(this);
        return m;
    }
    
    
    public void actionPerformed(ActionEvent event)
    {
    	String ref=event.getActionCommand();
         if(ref.equals("Open"))
         {
          	  String mediaPath="";
              File fileUrl;
              JFileChooser fileSelect=new JFileChooser();
              fileSelect.setFileSelectionMode(JFileChooser.FILES_ONLY);
              int status= fileSelect.showOpenDialog(MediaGUI.this);
                  
                  fileUrl=fileSelect.getSelectedFile();
                  mediaPath=fileUrl.getAbsolutePath();
                  run1(mediaPath);
                 
           if (status == JFileChooser.APPROVE_OPTION)
           {
             File file =fileSelect.getSelectedFile();
             mediaPath=file.getAbsolutePath();
             
             dir=file.getParentFile();

             mp3files=dir.listFiles();
             mp3Count=0;
            
             for(int i=0;i<mp3files.length;i++)
             {
                 System.out.println("Songs="+mp3files[i]);

             }
           }
		
       }
        

         
         String song;
         File newFile;
         if(ref.equals("pre"))
         {
           try
           {
        	   if(mp3Count<=0)
        		   mp3Count=0;
        	   else
        	   {	
        		   mp3Count--;
        		   newFile=mp3files[mp3Count];
        		   song=newFile.getAbsolutePath();
        		   run1(song);
        	   }
           }
           catch(Exception e)
           {
               System.err.println("Try again:(Previous Song Not Found) " + e);
           }
           
         }
         String song1;
         if(ref.equals("next"))
         {

             try
             {
            	 if(mp3Count>=mp3files.length)
            		 mp3Count=mp3files.length;
            	 else 
            	 {	 mp3Count++;
                 newFile=mp3files[mp3Count];
                 song1=newFile.getAbsolutePath();
                 run1(song1);
            	 }
             }
             catch(Exception e)
             {
                 System.err.println("Try again:(Next Song Not Found) " + e);
             }
             
         }

         if(ref.equals("pause"))
         {
        	 mediaPlayer.getMediaPlayer().pause();
        //	 System.out.println("len:"+mediaPlayer.getMediaPlayer().getTitle());
        	 System.out.println("len:"+mediaPlayer.getMediaPlayer().getLength());
        //	 System.out.println("len:"+mediaPlayer.getMediaPlayer().getMediaMeta());
        	 
         }
         if(ref.equals("play"))
         {
        	 mediaPlayer.getMediaPlayer().play();
        	
         }
         if(ref.equals("Forward"))
         {
        	 long tm=mediaPlayer.getMediaPlayer().getTime();
        	 System.out.println("In Forward time="+tm);  	 
        	 mediaPlayer.getMediaPlayer().setTime(tm+5000);
        	
         }
         if(ref.equals("Backward"))
         {
        	 long tm=mediaPlayer.getMediaPlayer().getTime();
        	 System.out.println("In Backward time="+tm); 
        	 mediaPlayer.getMediaPlayer().setTime(tm-5000);
        	
         }
         if(ref.equals("up"))
         {
        	 int vol=mediaPlayer.getMediaPlayer().getVolume();
        	 if(vol<=200)
        	 {	 
        		int up=vol+10;
        		mediaPlayer.getMediaPlayer().setVolume(up);
         	 	System.out.println("volumn up="+vol);
        		System.out.println("volumn update="+up);
        	 }
        	 
         }
         if(ref.equals("down"))
         {
        	 int vol=mediaPlayer.getMediaPlayer().getVolume();
        	 if(vol>=0)
        	 {
        		 int down=vol-10;
        		 mediaPlayer.getMediaPlayer().setVolume(down);
        		 System.out.println("volumn down="+vol);
        		 System.out.println("volumn down update="+down);
        	 }
         }
     	 if(ref.equals("GestureOpen"))
         {
        	 new GestureWebCam();
        //	 new NewCam();
  //      	 new NewJFrame().setVisible(true);
        	 System.out.println("In Gesture");
         }
    }
    public  void Wifi(String recstr)
    {
    	if(recstr.equals("PlaySong"))
    	{
    		mediaPlayer.getMediaPlayer().play();
    	}
    	if(recstr.equals("NextSong"))
    	{
    		try
            {
           	 if(mp3Count>=mp3files.length)
           		 mp3Count=mp3files.length;
           	 else 
           	 {	 mp3Count++;
                newFile=mp3files[mp3Count];
                song1=newFile.getAbsolutePath();
                run1(song1);
           	 }
            }
            catch(Exception e)
            {
                System.err.println("Try again:(Next Song Not Found) " + e);
            }
           
    	}
    	if(recstr.equals("PreviousSong"))
    	{
    		try
            {
         	   if(mp3Count<=0)
         		   mp3Count=0;
         	   else
         	   {	   mp3Count--;
              newFile=mp3files[mp3Count];
              song=newFile.getAbsolutePath();
              run1(song);
         	   }
            }
            catch(Exception e)
            {
                System.err.println("Try again:(Previous Song Not Found) " + e);
            }
    	}
    	if(recstr.equals("PauseSong"))
    	{
    		 mediaPlayer.getMediaPlayer().pause();
    	}
    	if(recstr.equals("Forward"))
    	{
    		long tm=mediaPlayer.getMediaPlayer().getTime();
    		System.out.println("In Forward time="+tm);  	 
       	 	mediaPlayer.getMediaPlayer().setTime(tm+5000);
       	
    	}
    	if(recstr.equals("Backward"))
    	{
    		long tm=mediaPlayer.getMediaPlayer().getTime();
    		System.out.println("In Backward time="+tm);  	 
       	 	mediaPlayer.getMediaPlayer().setTime(tm-5000);
       	
    	}
    	if(recstr.equals("VolUp"))
    	{
    		int vol=mediaPlayer.getMediaPlayer().getVolume();
       	 	if(vol<=200)
       	 	{	 
       	 		int up=vol+10;
       	 		mediaPlayer.getMediaPlayer().setVolume(up);
        	 	System.out.println("volumn up="+vol);
        	 	System.out.println("volumn update="+up);
       	 	}
    	}
    	if(recstr.equals("VolDown"))
    	{
    		 int vol=mediaPlayer.getMediaPlayer().getVolume();
        	 if(vol>=0)
        	 {
        		 int down=vol-10;
        		 mediaPlayer.getMediaPlayer().setVolume(down);
        		 System.out.println("volumn down="+vol);
        		 System.out.println("volumn down update="+down);
        	 }
        
    	}
    	
    }
   
    public void run1(String medp)
    {
        mediaPlayer.getMediaPlayer().playMedia(medp);
        try
        {
        	Thread.sleep(100);
        }
        catch(Exception e)
        {
        	
        }
        long tm=mediaPlayer.getMediaPlayer().getLength();        
        String ref=""+tm;
        System.out.println("leng ref:"+ref);
        int le=ref.length();
        String f=ref.substring(0,le-3);
        int inew=Integer.parseInt(f);
        js.setMaximum(inew);
        
        System.out.println("leng final:"+inew+"  min="+js.getMinimum()+"  max="+js.getMaximum());
        NewThread nth= new NewThread(inew);
        
    }
 
    public static void main(String []args)
    {
    	
    	final MediaGUI medGui=new MediaGUI(); 
        medGui.setVisible(true);
        Thread t=new Thread()
  		{
  			public void run()
  			{
  				System.out.println("Server ready to listen.....");
  				try
  				{
  					ServerSocket ss=new ServerSocket(1000);
  					while(true)
  					{
  						Socket s=ss.accept();
  						DataInputStream di=new DataInputStream(s.getInputStream());
  						String rev=di.readUTF();
  						medGui.Wifi(rev);
  						System.out.println("Received from Client:"+rev);
  						di.close();
  						s.close();	
  					}
  				}
  				catch(Exception e)
  				{
  					e.printStackTrace();
  				}
  			}
  		};
  		t.start();        
    }
}

class NewThread extends JFrame implements Runnable
{
	JSlider jsn;
	int time;
	Thread th;
	MediaGUI medGui1=new MediaGUI();
	NewThread(int reftime)
	{
		setSize(850,100);
		setLayout(null);
		setTitle("Progress Sider");
		setVisible(true);
		jsn=new JSlider();
	//	jsn.setMajorTickSpacing(1);
    //    jsn.setMinorTickSpacing(1);
    //    jsn.setPaintTicks(true);
    //    jsn.setPaintLabels(true);
        jsn.setMinimum(0);
        jsn.setValue(0);
        jsn.setValueIsAdjusting(true);
        jsn.setMaximum(reftime);
        
        jsn.setBounds(0,0,800,50);
     //   MediaGUI me=new MediaGUI();
     //   me.panel2.add(jsn);
         add(jsn);
		time=reftime;
		th=new Thread(this);
		System.out.println("In Thread:"+time);
		th.start(); // Start the thread
	}
	public void run()
	{
		try 
		{		   
		       System.out.println("length1:"+time);
		       int i=0;
		       while(i<time)
		       {	
		    	  // medGui1.SliderUpdate(i);
		    	   jsn.setValue(i);
		    	   Thread.sleep(1000);
		    	   i++;
		    	   
		       }
		}
		catch (InterruptedException e)
		{
			System.out.println("Child interrupted.");
		}
	}
}


class GestureWebCam implements WebcamImageTransformer,WebcamMotionListener,ActionListener,MouseListener
{
	private static final JHGrayFilter RGB = new JHGrayFilter();
	public BufferedImage transform(BufferedImage image)
    {
		return RGB.filter(image, null);
	}

	public GestureWebCam()
    {
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.setImageTransformer((WebcamImageTransformer) this);
		webcam.open();

 		JFrame window = new JFrame("Gesture Control");
 		window.addMouseListener(this);
		WebcamPanel panel = new WebcamPanel(webcam);
		Panel p=new Panel();
		p.setSize(300,200);
		
		panel.setFPSDisplayed(true);
		panel.setFillArea(true);
//		window.setLayout(null);
		window.add(panel);
		Button start=new Button("Start Capture");
		start.addActionListener(this);
		p.add(start);
	//	window.add(p,BorderLayout.SOUTH);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        WebcamMotionDetector detector = new WebcamMotionDetector(Webcam.getDefault());
		detector.setInterval(1000); //check every 1000 microsec
		detector.addMotionListener((WebcamMotionListener) this);
		detector.start();
   
	}
	public void actionPerformed(ActionEvent ae)
	{
		
	}
	int incr=0;
	String st="test";
	public void mouseClicked(MouseEvent me)
	{
		Webcam.setAutoOpenMode(true);
		BufferedImage image = Webcam.getDefault().getImage();
		incr++;
		try
		{// save image to PNG fil
			ImageIO.write(image,"PNG",new File("F:\\Eclipse Project\\test"+incr+".png"));
			System.out.println("Captured..");
			
			// Load the image (it is hard-coded here to make the code simpler).
			      
			       BufferedImage i = ImageIO.read(new File("F:\\Eclipse Project\\test"+incr+".png"));
			       compressAndShow(i,0.0f,incr); 
	   
		}
		catch(Exception e)
		{
			System.out.println("In Capture Error:"+e);
		}
		
		
	}
	public static void compressAndShow(BufferedImage image, float quality,int incr1) throws IOException
	{
	       // Get a ImageWriter for jpeg format.
	       Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("jpeg");
	       if (!writers.hasNext()) throw new IllegalStateException("No writers found");
	       ImageWriter writer = (ImageWriter) writers.next();
	       // Create the ImageWriteParam to compress the image.
	       ImageWriteParam param = writer.getDefaultWriteParam();
	       param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	       param.setCompressionQuality(quality);
	       // The output will be a ByteArrayOutputStream (in memory)
	       ByteArrayOutputStream bos = new ByteArrayOutputStream(32768);
	       ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
	       writer.setOutput(ios);
	       writer.write(null, new IIOImage(image, null, null),param);
	       ios.flush(); // otherwise the buffer size will be zero!
	       // From the ByteArrayOutputStream create a RenderedImage.
	       ByteArrayInputStream in = new ByteArrayInputStream(bos.toByteArray());
	       RenderedImage out = ImageIO.read(in);
	       int size = bos.toByteArray().length;
	       
	       ImageIO.write(out,"PNG",new File("F:\\Eclipse Project\\testnew"+incr1+".png"));
			
	       showImage("Compressed to " + quality + ": " + size + " bytes", out);
	       // Uncomment code below to save the compressed files.
	   //    File file = new File("compressed."+quality+".jpeg");
	   //    FileImageOutputStream output = new FileImageOutputStream(file);
	   //    writer.setOutput(output); writer.write(null, new IIOImage(image, null,null), param);
    }
	private static void showImage(String title,RenderedImage image)
	{
	       JFrame f = new JFrame(title);
	       f.getContentPane(); 
	       f.pack();
	       f.setVisible(true);
	       f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } 
	
	public void mouseEntered(MouseEvent me)
	{
		
	}
	public void mouseExited(MouseEvent me)
	{
		
	}
	public void mousePressed(MouseEvent me)
	{
		
	}
	public void mouseReleased(MouseEvent me)
	{
		
	}
	
    int i=0;
    public void motionDetected(WebcamMotionEvent wme)
    {
        i++;
		System.out.println("Motion is Detected"+i);
	}
}

