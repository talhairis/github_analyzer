package G221210002;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
*
* @author Talha İris talha.iris@ogr.sakarya.edu.tr
* @since 01.04.2024
* <p>
* GitAnalysis sınıfı tarafından çağrılan ve ayrıştırılmış java sınıfları üzerinde gerekli işlemleri uygulayan sınıf
* </p>
*/

public class JavaClassFiles {
	private File file;
	private String fileName;
	private int javaDocCommentLines;
	private int commentLines;
	private int codeLines;
	private int LOC;
	private int functions;
	private String commentLinesDevPerc;
	
	public JavaClassFiles(File file) {
		this.file = file;
		this.fileName = file.getName();
		this.javaDocCommentLines = countJavaDocCommentLines();
		this.commentLines = countCommentLines();
		this.codeLines = countCodeLines();
		this.LOC = countLOC();
		this.functions = countFunctions();
		this.commentLinesDevPerc = commentLinesDevPerc();
	}
	
	private int countJavaDocCommentLines() {
        int cjavaDocCommentLines = 0;
        boolean inCommentBlock = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("/**")) {
                    inCommentBlock = true;
                    if(line.startsWith("*")) {
                    	cjavaDocCommentLines++;
                    }
                } else if (line.startsWith("*/")) {
                    inCommentBlock = false;
                } else if (inCommentBlock) {
                	cjavaDocCommentLines++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cjavaDocCommentLines;
    }
	
	private int countCommentLines() {
        int ccommentLines = 0;
        boolean isMultiLineComment = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (isMultiLineComment) {
                    if (line.contains("*/")) {
                        isMultiLineComment = false;
                    }
                    else {
                    	ccommentLines++;
                    }
                } else {
                    if (line.startsWith("/*") && !line.startsWith("/**")) {
                        if (!line.contains("*/")) {
                            isMultiLineComment = true;
                        }
                        else {
                        	ccommentLines++;
                        }
                    } else if (line.contains("//")) {
                        ccommentLines++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ccommentLines;
    }

	private int countCodeLines() {
		int ccodeLines = 0;
		boolean isMultiLineComment = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (isMultiLineComment) {
                    if (line.contains("*/")) {
                        isMultiLineComment = false;
                    }
                } else {
                    if (line.startsWith("/*") || line.startsWith("/**")) {
                        if (!line.contains("*/")) {
                            isMultiLineComment = true;
                        }
                    }
                    else if(line.startsWith("//")) {
                    }
                    else if(!line.isEmpty()) {
                    	ccodeLines++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ccodeLines;
	}
	
	private int countLOC() {
		int cLOC = 0;
		boolean isMultiLineComment = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (isMultiLineComment) {
                    if (line.contains("*/")) {
                        isMultiLineComment = false;
                    }
                } else {
                    if (line.startsWith("/*") && line.startsWith("/**") && line.contains("//")) {
                        if (!line.contains("*/")) {
                            isMultiLineComment = true;
                        }
                    }
                    else {
                    	cLOC++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cLOC;
	}
	
	private int countFunctions() {
		int cfunctions = 0;
		
		try {
            List<String> lines = Files.readAllLines(file.toPath());
            boolean inFunction = false;
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("public") || line.startsWith("private") || line.startsWith("protected") || line.contains("static") || line.contains("void")) {
                	if(line.contains("(") && line.contains(")")) {
                		inFunction = true;
                	}
                }
                if(inFunction && line.contains("{")) {
                	cfunctions++;
                	inFunction = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return cfunctions;
	}

	private String commentLinesDevPerc() {
		double YG, YH, ccommentLinesDevPerc;
		YG = ((javaDocCommentLines + commentLines) * 0.8) / functions;
		YH = ((double)codeLines / functions) * 0.3;
		ccommentLinesDevPerc = ((100 * YG) / YH) - 100;
		String editedPerc = String.format("%.2f", ccommentLinesDevPerc);
		return editedPerc;
	}
	
	@Override
	public String toString() {
		return "Sınıf: " + fileName +
			   "\nJavadoc Satır Sayısı: " + javaDocCommentLines +
			   "\nYorum Satır Sayısı: " + commentLines +
			   "\nKod Satır Sayısı: " + codeLines +
			   "\nLOC: " + LOC +
			   "\nFonksiyon Sayısı: " + functions +
			   "\nYorum Sapma Yüzdesi: %" + commentLinesDevPerc;
	}
}
