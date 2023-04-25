import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.fs.FileSystem;
import java.util.Scanner;
import org.apache.hadoop.fs.*;
import java.net.URI;




import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class IPwordcount {

	public static void main(String[] args) throws Exception {
            Configuration conf = new Configuration();
	    Job job = Job.getInstance(conf, "IPwordcount");
	    job.setJarByClass(IPwordcount.class);
	    job.setMapperClass(IPwmapper.class);
	    job.setReducerClass(IPwreducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	          
           job.waitForCompletion(true);
		
		FileSystem fs =FileSystem.get(URI.create("hdfs://localhost:9000"+args[1]),conf);
		Path fname =fs.listStatus(new Path(args[1]))[1].getPath();
		FSDataInputStream in=null;
		
		in = fs.open(fname);
		
		Scanner Reader = new Scanner(in);
		String key = "";
               int maxi = 0;
        
               while (Reader.hasNextLine()) {
                   String data = Reader.nextLine();
                   String[] vals = data.split("	");
                   int count = Integer.parseInt(vals[1]);
                   if(count > maxi) {
            	     key = vals[0];
            	     maxi = count;
              }
        }
        
        System.out.println("The user which has logged max number of time is " + key+ "  " + maxi +" number of times");
        Reader.close();
            	
    }

}