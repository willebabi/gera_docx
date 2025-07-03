Claro\! Gerar o README em Markdown é o formato ideal para o GitHub. Ele ficará legível e bem estruturado diretamente no repositório.

Aqui está o README completo para o seu projeto Java, formatado em Markdown, pronto para ser copiado e colado em um arquivo `README.md` no GitHub:

-----

# Gerador de Documentos DOCX

-----

Este projeto Java permite gerar documentos **`.docx` dinamicamente** a partir de um **template DOCX** e um **arquivo JSON de dados**. Ele substitui placeholders no template e popula tabelas com listas de itens, tornando a criação de documentos padronizados rápida e automatizada.

## 🚀 Funcionalidades

  * **Substituição de Placeholders**: Troca textos como `{{nome_cliente}}` no seu template DOCX pelos valores correspondentes do JSON.
  * **População de Tabelas**: Adiciona linhas em tabelas predefinidas no template, usando dados de listas no JSON. Isso é perfeito para criar tabelas dinâmicas de produtos, serviços ou itens de projeto.
  * **Execução via Linha de Comando**: Dá pra rodar o projeto facilmente direto do terminal, passando os caminhos dos arquivos que você precisa como argumentos.

## 🛠️ Tecnologias Utilizadas

  * **Java 11+**: A linguagem de programação que a gente usou.
  * **Apache POI**: A biblioteca principal pra gente manipular os arquivos `.docx` (Word).
  * **Jackson**: Biblioteca pra fazer o *parse* (leitura) dos arquivos JSON.
  * **Maven**: A ferramenta de automação de *build* e gerenciamento de dependências.

-----

## 📦 Como Usar

### Pré-requisitos

Antes de começar, certifique-se de ter:

  * **JDK (Java Development Kit) 11 ou superior** instalado na sua máquina.
  * **Maven** instalado (ou você pode usar o Maven Wrapper, se preferir).

### 1\. Estrutura do Projeto

Organize seu projeto Java seguindo esta estrutura de pastas:

```
seu_projeto_java/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── seuprojeto/
│                   └── DocumentGenerator.java
│       └── resources/
│           └── templates/
│               └── meu_template.docx  <- Seu template DOCX
├── dados/
│   └── meus_dados.json               <- Seu arquivo JSON de dados
├── pom.xml
```

### 2\. Configuração do `pom.xml`

Garanta que seu arquivo `pom.xml` (na raiz do projeto) contém as dependências e a configuração pra gerar um JAR executável:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.seuprojeto</groupId>
    <artifactId>document-generator</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <poi.version>5.2.5</poi.version> <jackson.version>2.17.1</jackson.version> </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>${poi.version}</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>${jackson.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.3.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                            <mainClass>br.com.geradocx.Main</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>3.6.1</version>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/lib</outputDirectory>
                            <overWriteReleases>false</overWriteReleases>
                            <overWriteSnapshots>false</overWriteSnapshots>
                            <overWriteIfNewer>true</overWriteIfNewer>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.7.1</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>br.com.geradocx.Main</mainClass>
                        </manifest>
                    </archive>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### 3\. Crie Seu Template DOCX (`meu_template.docx`)

Crie um arquivo `.docx` com este conteúdo e salve-o em `src/main/resources/templates/`:

  * **Placeholders Gerais**: Use a sintaxe `{{chave}}` para dados simples (ex: `{{nome_cliente}}`).
  * **Tabela para Listas**: Crie uma tabela. A **primeira linha** deve ser o cabeçalho. A **segunda linha** deve conter os placeholders para *um único item* da sua lista (ex: `{{item_descricao}}`, `{{item_horas}}`). O script vai duplicar e preencher esta segunda linha pra cada item do seu JSON.

Exemplo de Template:

```
Caro(a) {{nome_cliente}},

Temos o prazer de apresentar a proposta para o projeto {{nome_projeto}}.

Detalhes do Projeto:
Tipo: {{tipo_projeto}}
Data de Início: {{data_inicio}}
Valor Estimado: R$ {{valor_projeto}}

Itens do Projeto:

| Descrição           | Horas Estimadas    | Status          |
|---------------------|--------------------|-----------------|
| {{item_descricao}}  | {{item_horas}}     | {{item_status}} |

Agradecemos o seu interesse.

Atenciosamente,

{{nome_empresa}}
```

### 4\. Crie Seu Arquivo JSON de Dados (`meus_dados.json`)

Crie um arquivo `.json` com os dados a serem inseridos. Salve-o na pasta `dados/`. As chaves JSON precisam ser iguais aos placeholders do seu template. Pra lista, use uma chave que contenha um array de objetos.

Exemplo de Dados JSON:

```json
{
    "nome_cliente": "João da Silva",
    "nome_projeto": "Implantação de ERP",
    "tipo_projeto": "Consultoria",
    "data_inicio": "01/08/2025",
    "valor_projeto": "50.000,00",
    "nome_empresa": "Sua Consultoria Ltda.",
    "itens_projeto_tabela": [
        {
            "item_descricao": "Levantamento de processos",
            "item_horas": "100",
            "item_status": "Concluído"
        },
        {
            "item_descricao": "Configuração do sistema",
            "item_horas": "200",
            "item_status": "Em Andamento"
        },
        {
            "item_descricao": "Treinamento de usuários",
            "item_horas": "80",
            "item_status": "Não Iniciado"
        }
    ]
}
```

### 5\. Código Java (`DocumentGenerator.java`)

Crie a classe `DocumentGenerator.java` no caminho `src/main/java/com/seuprojeto/`.

```java
package com.seuprojeto;

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

public class DocumentGenerator {

    public static void main(String[] args) {
        // Validação dos argumentos da linha de comando
        if (args.length != 3) {
            System.out.println("Uso: java -jar document-generator-1.0-SNAPSHOT-jar-with-dependencies.jar <caminho_template_docx> <caminho_json_dados> <caminho_saida_docx>");
            System.out.println("Exemplo: java -jar document-generator-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/resources/templates/meu_template.docx dados/meus_dados.json saida/documento_final.docx");
            return;
        }

        String templatePath = args[0];
        String jsonPath = args[1];
        String outputDocxPath = args[2];

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
                // Assumimos que a tabela que queremos popular tem pelo menos 2 linhas
                // (cabeçalho + linha de placeholder)
                if (table.getNumberOfRows() >= 2) {
                    // A segunda linha (índice 1) é a nossa linha de template
                    XWPFTableRow templateRow = table.getRow(1);
                    
                    // Verificar se a linha de template contém algum dos nossos placeholders de item
                    if (templateRow.getCell(0).getText().contains("{{item_descricao}}")) {
                        tableProcessed = true;
                        
                        // Obter os dados da lista do JSON
                        JsonNode itemsNode = jsonData.get("itens_projeto_tabela"); // Nome da chave da sua lista no JSON

                        if (itemsNode != null && itemsNode.isArray()) {
                            for (JsonNode item : itemsNode) {
                                XWPFTableRow newRow = table.createRow(); // Adiciona uma nova linha no final da tabela
                                
                                // Preencher as células da nova linha
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
                        // Limpa o conteúdo da linha de template original
                        for(XWPFTableCell cell : templateRow.getTableCells()) {
                            for(int i = cell.getParagraphs().size() - 1; i >= 0; i--) {
                                cell.removeParagraph(i);
                            }
                        }
                        templateRow.getCell(0).setText(""); 

                        break; // Sai do loop de tabelas após encontrar e processar a tabela desejada
                    }
                }
            }

            if (!tableProcessed) {
                System.out.println("Aviso: Nenhuma tabela com o placeholder de item '{{item_descricao}}' foi encontrada no template.");
            }

            // 5. Salvar o novo documento
            Path outputDocxPathDir = Paths.get(outputDocxPath).getParent();
            if (outputDocxPathDir != null) {
                Files.createDirectories(outputDocxPathDir); // Cria o diretório de saída se não existir
            }
            try (FileOutputStream out = new FileOutputStream(outputDocxPath)) {
                document.write(out);
            }
            System.out.println("Documento gerado com sucesso em: " + outputDocxPath);

        } catch (Exception e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

### 6\. Compilar e Gerar o JAR

1.  Abra seu **terminal** na raiz do projeto (`seu_projeto_java/`).

2.  Execute este comando Maven pra compilar o código e gerar o JAR executável:

    ```bash
    mvn clean package
    ```

    Isso vai criar uma pasta `target/` no seu projeto, e dentro dela você vai encontrar o arquivo JAR, tipo `document-generator-1.0-SNAPSHOT-jar-with-dependencies.jar`.

### 7\. Executar o Projeto

No terminal, ainda na raiz do seu projeto, execute o JAR passando os três argumentos necessários:

```bash
java -jar target/document-generator-1.0-SNAPSHOT-jar-with-dependencies.jar \
src/main/resources/templates/meu_template.docx \
dados/meus_dados.json \
saida/documento_final.docx
```

  * `src/main/resources/templates/meu_template.docx`: É o caminho pro seu arquivo de **template DOCX**.
  * `dados/meus_dados.json`: É o caminho pro seu arquivo **JSON de dados**.
  * `saida/documento_final.docx`: É o caminho e nome do **arquivo DOCX de saída** que será gerado.

O programa vai criar o arquivo `documento_final.docx` na pasta `saida/` (que ele cria se não existir), com todos os dados substituídos.

-----

## 🤝 Contribuição

Sinta-se à vontade pra fazer um "fork" (cópia) deste repositório, propor melhorias ou relatar problemas. Sua ajuda é bem-vinda\!