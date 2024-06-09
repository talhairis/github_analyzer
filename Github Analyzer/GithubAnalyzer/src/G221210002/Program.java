package G221210002;

import java.util.Scanner;

/**
*
* @author Talha İris talha.iris@ogr.sakarya.edu.tr
* @since 01.04.2024
* <p>
* Son kullanıcı tarafından ulaşılabilecek program sınıfı
* </p>
*/


public class Program {

	public static void main(String[] args) {
		//https://github.com/mfadak/Odev1Ornek
		//https://github.com/ahmetkeremburak/Lucky-Number-Game
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Analiz edilecek github deposunu giriniz: ");
			String url = scanner.nextLine();
			System.out.print("Klonlanacak dosya adını giriniz: ");
			String fileName = scanner.nextLine();
			
			GitAnalysis gitAnalysis = new GitAnalysis(url, fileName);
			gitAnalysis.analysisRunner();
			System.out.println(gitAnalysis);
		}
	}

}