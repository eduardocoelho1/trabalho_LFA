import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Glc2fnc {
    public static void main(String[] args) {
        Gramatica gram = new Gramatica();
        
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(" -> "); // separa a linha por " -> "
                String nome = partes[0]; // recebe a parte antes de " -> "
                String[] prodTemp = partes[1].split(" \\| "); // recebe a parte depois de " -> " e separa os elementos por " | "
                Set<String> producoes = new HashSet<>(Arrays.asList(prodTemp));
                gram.adiciona(nome, producoes);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo nao encontrado");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Falha na leitura");
            System.exit(1);
        }
    }
}
