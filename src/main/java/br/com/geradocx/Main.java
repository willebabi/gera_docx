package br.com.geradocx;

import org.apache.poi.xwpf.usermodel.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Validação dos argumentos da linha de comando
        if (args.length != 3) {
            System.out.println("Uso: java -jar document-generator-1.0-SNAPSHOT-jar-with-dependencies.jar <caminho_template_docx> <caminho_json_dados> <caminho_saida_docx>");
            System.out.println("Exemplo: java -jar document-generator-1.0-SNAPSHOT-jar-with-dependencies.jar templates/meu_template.docx dados/meus_dados.json saida/documento_final.docx");
            return;
        }

        String templatePath = args[0];
        String jsonPath = args[1];
        String outputPath = args[2];

        try {
            // 1. Carregar os dados do JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonData = mapper.readTree(new FileInputStream(jsonPath));

            // 2. Carregar o documento DOCX de template
            XWPFDocument document;
            try (InputStream is = new FileInputStream(templatePath)) {
                document = new XWPFDocument(is);
            }

            // 3. Substituir placeholders em parágrafos
            for (XWPFParagraph p : document.getParagraphs()) {
                String text = p.getText();
                for (Iterator<Map.Entry<String, JsonNode>> it = jsonData.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    String key = entry.getKey();
                    JsonNode valueNode = entry.getValue();

                    // Substitui apenas se não for um array (lista)
                    if (!valueNode.isArray()) {
                        String placeholder = "{{" + key + "}}";
                        if (text.contains(placeholder)) {
                            text = text.replace(placeholder, valueNode.asText());
                        }
                    }
                }
                // Limpa os runs existentes e adiciona um novo com o texto atualizado
                for (int i = p.getRuns().size() - 1; i >= 0; i--) {
                    p.removeRun(i);
                }
                p.createRun().setText(text);
            }

            // 4. Encontrar a tabela e popular com os itens da lista
            boolean tableProcessed = false;
            for (XWPFTable table : document.getTables()) {
                if (table.getNumberOfRows() >= 2) {
                    XWPFTableRow templateRow = table.getRow(1); // A segunda linha (índice 1) é o template

                    // Verifica se a linha de template contém o placeholder de item (ex: {{item_descricao}})
                    // para identificar a tabela correta
                    if (templateRow.getCell(0).getText().contains("{{item_descricao}}")) {
                        tableProcessed = true;
                        JsonNode itemsNode = jsonData.get("itens_projeto_tabela"); // Nome da chave da sua lista no JSON

                        if (itemsNode != null && itemsNode.isArray()) {
                            for (JsonNode item : itemsNode) {
                                XWPFTableRow newRow = table.createRow(); // Adiciona uma nova linha
                                for (int i = 0; i < templateRow.getTableCells().size(); i++) {
                                    XWPFTableCell newCell = newRow.getCell(i);
                                    if (newCell == null) { // Garante que a célula exista
                                        newCell = newRow.addNewTableCell();
                                    }
                                    
                                    // Mapear a célula para o placeholder correspondente
                                    // Assumimos a ordem das colunas: Descrição, Horas, Status
                                    if (i == 0) { // Coluna da Descrição
                                        newCell.setText(item.get("item_descricao").asText("N/A"));
                                    } else if (i == 1) { // Coluna das Horas
                                        newCell.setText(item.get("item_horas").asText("N/A"));
                                    } else if (i == 2) { // Coluna do Status
                                        newCell.setText(item.get("item_status").asText("N/A"));
                                    }
                                    // Adicione mais 'else if' para mais colunas
                                }
                            }
                        }
                        // Remover a linha de template original
                        // Apache POI não tem um método direto `removeRow`.
                        // A estratégia mais comum é limpar o conteúdo da linha de template, ou
                        // se a linha de template DEVE ser removida, precisaria de uma manipulação
                        // de baixo nível do XML DOCX, ou uma cópia da tabela sem a linha.
                        // Para simplificar, vamos apenas limpar o texto da linha de template.
                        for(XWPFTableCell cell : templateRow.getTableCells()) {
                            for(int i = cell.getParagraphs().size() - 1; i >= 0; i--) {
                                cell.removeParagraph(i);
                            }
                        }
                        // Adicionar um parágrafo vazio para garantir que a célula não suma
                        templateRow.getCell(0).setText(""); 
                        // Outra abordagem (mais complexa para remover a linha):
                        // table.removeRow(1); // Isso pode causar problemas com índices se houver mais tabelas

                        break; // Sai do loop de tabelas após processar a correta
                    }
                }
            }

            if (!tableProcessed) {
                System.out.println("Aviso: Nenhuma tabela com o placeholder de item '{{item_descricao}}' foi encontrada no template.");
            }

            // 5. Salvar o novo documento
            Path outputPathDir = Paths.get(outputPath).getParent();
            if (outputPathDir != null) {
                Files.createDirectories(outputPathDir); // Cria o diretório de saída se não existir
            }
            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                document.write(out);
            }
            System.out.println("Documento gerado com sucesso em: " + outputPath);

        } catch (Exception e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}