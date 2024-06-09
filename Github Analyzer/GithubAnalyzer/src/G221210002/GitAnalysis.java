package G221210002;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
*
* @author Talha İris talha.iris@ogr.sakarya.edu.tr
* @since 01.04.2024
* <p>
* Arka planda github ve klonlanacağı dosyayı oluşturan, java sınıflarını ayrıştırıp gereken fonksiyonları çağıran sınıf
* </p>
*/

public class GitAnalysis {
    private String url;
    private String cloneDirectoryPath;
    private ArrayList<JavaClassFiles> javaClasses;
    
    public GitAnalysis(String url, String fileName) {
    	this.url = url;
    	this.cloneDirectoryPath = System.getProperty("user.dir")+ File.separator + fileName;
    	this.javaClasses = new ArrayList<>();
    }
    
    // Creates a file to clone a repository
    private File fileCreation() {
    	File newDirectory;
        do {
        	newDirectory = new File(cloneDirectoryPath);
            if (newDirectory.exists()) {
                System.out.println("Bu isimde bir dosya zaten var. Lütfen farklı bir dosya adı girin.");
            }
        } while (newDirectory.exists());
        return newDirectory;
    }

    //Clones the repository to a file
    private void cloneGit() {
        File cloneDirectory = fileCreation();
        cloneDirectory.mkdirs();
        
        try {
            Process process = Runtime.getRuntime().exec("git clone " + url + " " + cloneDirectory.getAbsolutePath());
            process.waitFor();
            System.out.println("Klonlama başarılı.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        cloneDirectoryPath = cloneDirectory.getPath();
    }
    
    //Finds all of the classes contains class definition
    private static boolean containsClassDefinition(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }

        try {
            for (String line : Files.readAllLines(file.toPath())) {
                if (line.contains("class") && !line.contains("//")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //Extracts all of the classes from *.java type files
    private void extractClasses(File directory) {
    	if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Belirtilen dizin bulunamadı veya bir klasör değil.");
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                	extractClasses(file); // Recursively scans sub-directories
                } else if (file.getName().endsWith(".java") && containsClassDefinition(file)) {
                    JavaClassFiles newFile = new JavaClassFiles(file);
                    javaClasses.add(newFile);
                }
            }
        }
    }
    
    //Analysis runner
    public void analysisRunner() {
    	cloneGit();
    	File directory = new File(cloneDirectoryPath);
    	extractClasses(directory);
    }
    
    @Override
    public String toString() {
    	int i;
    	String javaClassesString = "------------------------------";
    	for(i = 0; i < javaClasses.size(); i++) {
    		javaClassesString += "\n" + javaClasses.get(i).toString();
    		javaClassesString += "\n------------------------------";
    	}
    	return javaClassesString;
    }

}