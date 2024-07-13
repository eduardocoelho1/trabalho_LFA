import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;

public class Gramatica {
    private Map<String, Set<String>> regras;
    
    public Gramatica() {
        regras = new LinkedHashMap<>();
    }
    
    public Gramatica(Map<String, Set<String>> r) {
        regras = new LinkedHashMap<>(r);
    }
    
    public void adiciona(String nome, Set<String> producoes) {
        if (regras.get(nome) != null) {
            Set<String> prodAntigas = new HashSet<>(regras.get(nome));
            producoes.addAll(prodAntigas);
        }    
        regras.put(nome, producoes);
    }
    
    public void print() {
        for (Map.Entry<String,Set<String>> entry: regras.entrySet()) {
            String nome = entry.getKey();
            Set<String> producoes = entry.getValue();
            System.out.print(nome + " -> ");
            boolean primeiro = true;
            for (String prodAtual: producoes) {
                if (!primeiro) {
                    System.out.print(" | " + prodAtual);
                } else {
                    System.out.print(prodAtual);
                    primeiro = false;
                }
            }
            System.out.println();
        }
    }

    public Gramatica removeIniRec() {
        Map.Entry<String, Set<String>> regraIni = regras.entrySet().iterator().next();
        String nomeIni = regraIni.getKey();
        Set<String> prodIni = regraIni.getValue();

        Iterator<String> prodIter = prodIni.iterator();
        boolean recursao = false;
        while (prodIter.hasNext() && !recursao) {
            String prodAtual = prodIter.next();
            int i = 0;
            while (i < prodAtual.length() && !recursao) {
                if (Character.toString(prodAtual.charAt(i)).equals(nomeIni)) {
                    recursao = true;
                }
                i++;
            }
        }
        
        if (recursao) {
            Set<String> prodIniNovo = new HashSet<>();
            prodIniNovo.add(nomeIni);
            Map<String, Set<String>> regrasNovo = new LinkedHashMap<>();
            regrasNovo.put("S'", prodIniNovo); // o início antigo passa a ser produção do novo
            regrasNovo.putAll(regras);
            Gramatica gramNovo = new Gramatica(regrasNovo);
            return gramNovo;
        } else {
            Gramatica gramNovo = new Gramatica(regras);
            return gramNovo;
        }
    }
    
    public Set<String> nullable() {
        Set<String> anulaveis = new HashSet<>();
        Set<String> anulAnterior = new HashSet<>();
        for (Map.Entry<String,Set<String>> regra: regras.entrySet()) {
            String nome = regra.getKey();
            Set<String> producoes = regra.getValue();
            Iterator<String> prodIter = producoes.iterator();
            boolean found = false;
            while (prodIter.hasNext() && !found) {
                if (prodIter.next().equals(".")) {
                    anulaveis.add(nome);
                    found = true;
                }
            }
        }
        
        if (anulaveis.isEmpty()) {
            return null;
        }
        
        do {
            anulAnterior.addAll(anulaveis);
            for (Map.Entry<String,Set<String>> regra: regras.entrySet()) {
                String nome = regra.getKey();
                Set<String> producoes = regra.getValue();

                for (String prodAtual: producoes) {
                    boolean produzLambda = true;
                    if (prodAtual.toLowerCase().equals(prodAtual)) {
                        produzLambda = false;
                    } else {
                        int i = 0;
                        while (i < prodAtual.length() && produzLambda) {
                            if (Character.isUpperCase(prodAtual.charAt(i)) 
                            && !anulAnterior.contains(String.valueOf(prodAtual.charAt(i)))) {
                                // se for variável e não estiver em "anulAnterior"
                                produzLambda = false;
                            }
                            i++;
                        }
                    }
                    if (produzLambda) {
                        anulaveis.add(nome);
                    }
                }
            }
        } while (!anulaveis.equals(anulAnterior));
        
        return anulaveis;
    }
}
