# Android Image Processing Toolkit

## Team: 
* William DeRaad
* Russell Mehring
* Derek Reiersen

### Using Opencv with java Buffered Image examples
```
    private BufferedImage blur(){
        byte[] resultPixels;
        Mat destination = new Mat();
        BufferedImage result;
        try {
            double width = image.getWidth();
            double height = image.getHeight();
            Mat source = new Mat(new Size(width,height), CvType.CV_8UC3); 
            MatOfByte resultBytes = new MatOfByte();
            byte[] sourcePixels = ((DataBufferByte)
                    image.getRaster().getDataBuffer()).getData();
            source.put(0, 0, sourcePixels);
            destination = new Mat(source.rows(),source.cols(),source.type());
            Imgproc.GaussianBlur(source, destination,new Size(25,25), 0);
            Highgui.imencode(".png", destination, resultBytes);
            resultPixels = resultBytes.toArray();
        }catch (Exception e) {
            e.printStackTrace();
            destination  = null;
            resultPixels = null;            
        }
        InputStream buffer = new ByteArrayInputStream(resultPixels);
        try{result = ImageIO.read(buffer);}
        catch(Exception e){
            System.out.println("Sharpen IO: "+e.getMessage());
            result = null;
        }
        return result;
    }
    
    private BufferedImage sharpen() {
        byte[] resultPixels;
        Mat destination = new Mat();
        BufferedImage result;
        try{
            double width = image.getWidth();
            double height = image.getHeight();
            Mat source = new Mat(new Size(width,height), CvType.CV_8UC3);
            MatOfByte resultBytes = new MatOfByte();
            byte[] sourcePixels = ((DataBufferByte)
                                  image.getRaster().getDataBuffer()).getData();

            source.put(0, 0, sourcePixels);
            destination = new Mat(source.rows(),source.cols(),source.type());
            Imgproc.GaussianBlur(source, destination, new Size(0,0), 10);
            Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);
            Highgui.imencode(".png", destination, resultBytes);
            resultPixels = resultBytes.toArray();
        }catch (Exception e) {
            e.printStackTrace();
            destination  = null;
            resultPixels = null;
        }
        InputStream buffer = new ByteArrayInputStream(resultPixels);
        try{result = ImageIO.read(buffer);}
        catch(Exception e){
            e.printStackTrace();
            result = null;
        }
        return result;

    }
    
    private BufferedImage edge(){
        byte[] resultPixels;
        Mat destination = new Mat();
        BufferedImage result;
        try{
            double width = image.getWidth();
            double height = image.getHeight();
            Mat source = new Mat(new Size(width,height),
                    Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            MatOfByte resultBytes = new MatOfByte();
            byte[] sourcePixels = ((DataBufferByte)
                                  image.getRaster().getDataBuffer()).getData();
            source.put(0, 0, sourcePixels);
            Mat temp = new Mat(source.rows(),source.cols(),source.type());
    	    destination = new Mat(source.rows(),source.cols(),source.type());   
    	    Imgproc.Laplacian(source, temp, CvType.CV_8UC1);
    	    org.opencv.core.Core.convertScaleAbs(temp, destination);
            Highgui.imencode(".png", destination, resultBytes);
            resultPixels = resultBytes.toArray();
        }catch (Exception e) {
            e.printStackTrace();
            destination  = null;
            resultPixels = null;
        }
        InputStream buffer = new ByteArrayInputStream(resultPixels);
        try{result = ImageIO.read(buffer);}
        catch(Exception e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }
        
    
    private BufferedImage histogram(){
    	byte[] resultPixels;
        Mat destination = new Mat();
        BufferedImage result;
    	try{
    		double width = image.getWidth();
            double height = image.getHeight();
            Mat source = new Mat(new Size(width,height), CvType.CV_8UC3);
            MatOfByte resultBytes = new MatOfByte();
            byte[] sourcePixels = ((DataBufferByte)
                    image.getRaster().getDataBuffer()).getData();
            source.put(0, 0, sourcePixels);
            destination = new Mat(source.rows(),source.cols(),source.type());
            Mat ycrcb = new Mat();
            Imgproc.cvtColor(source,ycrcb,Imgproc.COLOR_BGR2YCrCb);
            ArrayList<Mat> temp = new ArrayList<Mat>();
            org.opencv.core.Core.split(ycrcb, temp);
            Imgproc.equalizeHist(temp.get(0), temp.get(0));
            org.opencv.core.Core.merge(temp,ycrcb);
            Imgproc.cvtColor(ycrcb,destination,Imgproc.COLOR_YCrCb2BGR);
            Highgui.imencode(".png", destination, resultBytes);
            resultPixels = resultBytes.toArray();
        }catch (Exception e) {
            e.printStackTrace();
            destination  = null;
            resultPixels = null;
        }
    	InputStream buffer = new ByteArrayInputStream(resultPixels);
        try{result = ImageIO.read(buffer);}
        catch(Exception e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    private BufferedImage gray(){
        double width;
    	double height;
    	BufferedImage result = null;
    	try {
    		byte[] data = ((DataBufferByte)
    		        image.getRaster().getDataBuffer()).getData();
    		Mat mat = new Mat(
    		        image.getHeight(), image.getWidth(), CvType.CV_8UC3);
    		mat.put(0, 0, data);

    		Mat mat1 = new Mat(
    		        image.getHeight(), image.getWidth(), CvType.CV_8UC1);
    		
    		Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

    		byte[] data1 = new byte[mat1.rows() * mat1.cols() * 
    		                        (int)(mat1.elemSize())];
    		mat1.get(0, 0, data1);
    		result = new BufferedImage(
    		        mat1.cols(), mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
    	    result.getRaster().setDataElements(
    	            0, 0, mat1.cols(), mat1.rows(), data1);
        } catch(Exception e){
            e.printStackTrace();
        }
    	return result;
    }
    
    private BufferedImage bright( int bd ){
    	byte[] resultPixels;
        Mat destination = new Mat();
        BufferedImage result;
    	final double alpha = 1;
        final double beta = 30*bd;
        try{
        	double width = image.getWidth();
            double height = image.getHeight();
            Mat source = new Mat(new Size(width,height), CvType.CV_8UC3);
            MatOfByte resultBytes = new MatOfByte();
            byte[] sourcePixels = ((DataBufferByte)
                                  image.getRaster().getDataBuffer()).getData();

            source.put(0, 0, sourcePixels);
            destination = new Mat(source.rows(),source.cols(),source.type());
            source.convertTo(destination, -1, alpha, beta);
            Highgui.imencode(".png", destination, resultBytes);
            resultPixels = resultBytes.toArray();
        }catch (Exception e) {
            e.printStackTrace();
            destination  = null;
            resultPixels = null;
        }
        InputStream buffer = new ByteArrayInputStream(resultPixels);
        try{result = ImageIO.read(buffer);}
        catch(Exception e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }
    ```
