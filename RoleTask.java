import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//import java.io.;
//import java.lang.String;

public class RoleTask {

	// Main driver method
	public static void main(String[] args) throws IOException {

		///////////// List of all files in the directory ////////////

		File directoryPath = new File("C:\\Users\\Dell\\Downloads\\role");
		File filesList[] = directoryPath.listFiles();

		String outputFilename = "C:\\Users\\Dell\\Desktop\\JDK_ADK\\JPO_Practice\\RoleTaskOutput.csv";
		String fileName;
		FileWriter fw = new FileWriter(outputFilename);
		fw.append("Release,Product,Role,AppName\n");

		//////////// For loop for taking each file for extraction ////////////
		for (File file : filesList) {
			fileName = file.getPath();
			// Reading html file on local directory
			FileReader fr = new FileReader(fileName);
			// Try block to check exceptions
			try {

				BufferedReader br = new BufferedReader(fr);

				String role = "", release = "", product = "", appName = "";

				int count = 0;

				String line;

				//////////// Loop through each line of input file ////////////
				while ((line = br.readLine()) != null) {

					// Check if line contains role, product, release, or appName and extract it

					// get the Role name
					if (line.contains("\"roles\"")) {
						role = line.split("content=")[1].replaceAll("[\">]", "");
					}

					// get the Release name
					if (line.contains("\"release\"")) {
						release = line.split("content=")[1].replaceAll("[\">]", "");
					}

					// get the Product name
					if (line.contains("\"bigicon\"")) {
						String[] splitLine = line.split("/");
						String lastOne = splitLine[(splitLine.length) - 1];
//					lastOne = lastOne.replaceAll("[\">]", "");
						product = lastOne.split("_role")[0];
					}

					// get the App name
					if (line.contains("<li") && line.contains("</a></li>")) {
						String[] splitLine = line.split("\">");
						String lastOne = splitLine[(splitLine.length) - 1];
						appName = lastOne.replaceAll("</a></li>", "");
						count++;
						System.out.println(release + "  " + product + "  " + role + "  " + appName + "  " + count);

						// append the data to csv file
						fw.append(release + "," + product + "," + role + "," + appName + "\n");
					}

				}

				br.close();

			}

			// Catch block to handle exceptions
			catch (Exception ex) {

				// Exception of not finding the location and string reading termination the
				// function br.close();
				System.out.println(ex.getMessage());
			}
		}
		System.out.println("\nData successfully fetched and copied to " + outputFilename);

		// Closing the file writer after all the completion
		fw.close();
	}
}