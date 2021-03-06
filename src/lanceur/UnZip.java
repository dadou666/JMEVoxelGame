package lanceur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip {
	List<String> fileList;
	public String INPUT_ZIP_FILE;
	public String OUTPUT_FOLDER;

	public UnZip(String INPUT_ZIP_FILE, String OUTPUT_FOLDER) {
		this.INPUT_ZIP_FILE = INPUT_ZIP_FILE;
		this.OUTPUT_FOLDER = OUTPUT_FOLDER;
		unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
	}

	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param output
	 *            zip file output folder
	 */
	public void unZipIt(String zipFile, String outputFolder) {

		byte[] buffer = new byte[1024];

		try {

			// create output directory is not exists
			File folder = new File(OUTPUT_FOLDER);

			if (!folder.exists()) {
				folder.mkdir();
			} else {
				for (File f : folder.listFiles()) {
					if (!f.getName().endsWith(".zip")) {
						f.delete();
					}
				}
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				System.out.println("file unzip : " + fileName);
				int idx = fileName.indexOf("/");
				if (idx >= 0) {
					String newFileName = fileName.substring(idx + 1, fileName.length());
					if (!newFileName.equals(".gitattributes") && !newFileName.equals(".gitignore")) {
						File newFile = new File(outputFolder + File.separator + newFileName);

						System.out.println("file unzip : " + newFile.getAbsoluteFile());
						
						if (ze.isDirectory()) {
							newFile.mkdirs();
						} else {

							// create all non exists folders
							// else you will hit FileNotFoundException for
							// compressed
							// folder
							new File(newFile.getParent()).mkdirs();

							FileOutputStream fos = new FileOutputStream(newFile);

							int len;
							while ((len = zis.read(buffer)) > 0) {
								fos.write(buffer, 0, len);
							}

							fos.close();

						}
					}
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
			(new File(zipFile)).delete();
			System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}