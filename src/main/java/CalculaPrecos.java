import javax.swing.*; // Importa pacotes para criar a interface gráfica.
import java.awt.*; // Importa pacotes para o layout da interface gráfica.
import java.awt.event.ActionEvent; // Importa pacotes para eventos de ação.
import java.awt.event.ActionListener; // Importa pacotes para ouvir e responder aos eventos de ação.
import java.math.BigDecimal; // Importa a classe BigDecimal para cálculos de precisão.
import java.text.DecimalFormat; // Importa a classe DecimalFormat para formatação numérica.
import java.text.NumberFormat; // Importa a classe NumberFormat para formatação numérica.
import java.text.ParseException; // Importa a classe ParseException para tratamento de exceções de parsing.
import java.util.ArrayList; // Importa a classe ArrayList para criar listas dinâmicas.
import java.util.Locale; // Importa a classe Locale para configuração de localidade.
import java.math.RoundingMode; // Importa a classe RoundingMode para especificar modos de arredondamento.

public class CalculaPrecos { // Define a classe principal da aplicação.
    static class Produto { // Define uma classe aninhada para representar um produto.
        static int contador = 1; // Inicializa um contador estático para numerar os produtos.
        int numero; // Número do produto.
        String nome; // Nome do produto.
        int quantidade; // Quantidade de unidades.
        BigDecimal precoEmbalagem; // Preço total da embalagem.
        BigDecimal valorUnitario; // Preço por unidade.
        BigDecimal porcentagemLucro; // Porcentagem de lucro.
        BigDecimal precoFinal; // Preço de venda final por unidade.
        BigDecimal custoTotal; // Custo total da embalagem.

        Produto(String nome, int quantidade, BigDecimal precoEmbalagem, BigDecimal porcentagemLucro) {
            this.numero = contador++; // Atribui um número ao produto e incrementa o contador.
            this.nome = nome; // Define o nome do produto.
            this.quantidade = quantidade; // Define a quantidade de unidades.
            this.precoEmbalagem = precoEmbalagem; // Define o preço total da embalagem.
            this.porcentagemLucro = porcentagemLucro; // Define a porcentagem de lucro.
            this.custoTotal = precoEmbalagem; // Inicializa o custo total com o preço da embalagem.
            // Calcula o valor unitário dividindo o preço da embalagem pela quantidade de unidades.
            this.valorUnitario = precoEmbalagem.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP);
            // Calcula o lucro unitário multiplicando o valor unitário pela porcentagem de lucro.
            BigDecimal lucro = valorUnitario.multiply(porcentagemLucro.divide(BigDecimal.valueOf(100)));
            // Calcula o preço final adicionando o lucro ao valor unitário.
            this.precoFinal = valorUnitario.add(lucro);
        }

        @Override
        public String toString() { // Sobrescreve o método toString para formatar a saída do produto.
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
            return "Produto " + numero + ": " + nome +
                    ", Custo Total: " + format.format(custoTotal) +
                    ", Quantidade: " + quantidade +
                    ", Valor Unitário: " + format.format(valorUnitario) +
                    ", Porcentagem de Lucro: " + porcentagemLucro.setScale(0, RoundingMode.HALF_UP).toString() + "%" +
                    ", Preço de Venda por Unidade: " + format.format(precoFinal);
        }
    }

    public static void main(String[] args) {
        ArrayList<Produto> produtos = new ArrayList<>(); // Cria uma lista para armazenar produtos.

        SwingUtilities.invokeLater(() -> { // Cria a interface gráfica na thread de despacho de eventos.
            JFrame frame = new JFrame("Calculadora de Preços"); // Cria a janela principal.
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define a ação de fechar a janela.
            frame.setSize(500, 500); // Define o tamanho da janela.
            frame.setLayout(new BorderLayout(10, 10)); // Define o layout da janela.

            JPanel inputPanel = new JPanel(); // Cria um painel para entrada de dados.
            inputPanel.setLayout(new GridBagLayout()); // Define o layout do painel.
            inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adiciona bordas ao painel.
            GridBagConstraints gbc = new GridBagConstraints(); // Cria um objeto para especificar restrições de layout.
            gbc.insets = new Insets(5, 5, 5, 5); // Define os espaçamentos.
            gbc.fill = GridBagConstraints.HORIZONTAL; // Define a maneira de preencher os componentes.

            // Adiciona componentes de entrada de dados ao painel.
            gbc.gridx = 0;
            gbc.gridy = 0;
            inputPanel.add(new JLabel("Nome do Produto:"), gbc);
            JTextField nomeField = new JTextField();
            gbc.gridx = 1;
            gbc.gridy = 0;
            inputPanel.add(nomeField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            inputPanel.add(new JLabel("Quantidade de Unidades:"), gbc);
            JTextField quantidadeField = new JTextField();
            gbc.gridx = 1;
            gbc.gridy = 1;
            inputPanel.add(quantidadeField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            inputPanel.add(new JLabel("Preço da Embalagem:"), gbc);
            JTextField precoEmbalagemField = new JTextField();
            gbc.gridx = 1;
            gbc.gridy = 2;
            inputPanel.add(precoEmbalagemField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            inputPanel.add(new JLabel("Porcentagem de Lucro:"), gbc);
            JTextField porcentagemField = new JTextField();
            gbc.gridx = 1;
            gbc.gridy = 3;
            inputPanel.add(porcentagemField, gbc);

            JButton adicionarButton = new JButton("Adicionar Produto"); // Cria um botão para adicionar produtos.
            gbc.gridx = 1;
            gbc.gridy = 4;
            inputPanel.add(adicionarButton, gbc);

            JTextArea outputArea = new JTextArea(10, 30); // Cria uma área de texto para exibir produtos adicionados.
            outputArea.setEditable(false); // Define a área de texto como não editável.
            JScrollPane scrollPane = new JScrollPane(outputArea); // Adiciona uma barra de rolagem à área de texto.

            JPanel outputPanel = new JPanel(new BorderLayout()); // Cria um painel para exibir a lista de produtos.
            outputPanel.setBorder(BorderFactory.createTitledBorder("Produtos Adicionados")); // Adiciona uma borda com título ao painel.
            outputPanel.add(scrollPane, BorderLayout.CENTER); // Adiciona a área de texto ao painel.

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Cria um painel para o botão "Sobre".
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adiciona bordas ao painel.

            JButton sobreButton = new JButton("Sobre"); // Cria o botão "Sobre".
            sobreButton.addActionListener(new ActionListener() { // Adiciona um listener ao botão "Sobre".
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(frame,
                            "Desenvolvido por Tiago Sousa Rocha\n" +
                                    "Empresa: Ecron Systems\n" +
                                    "Email: tiagorocha.pa@gmail.com\n" +
                                    "LinkedIn: www.linkedin.com/in/tiago-sousa-rocha-2b6a40270\n" +
                                    "GitHub: https://github.com/TiagoSousaRocha",
                            "Sobre", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            bottomPanel.add(sobreButton); // Adiciona o botão "Sobre" ao painel inferior.

            frame.add(inputPanel, BorderLayout.NORTH); // Adiciona o painel de entrada à parte superior da janela.
            frame.add(outputPanel, BorderLayout.CENTER); // Adiciona o painel de saída ao centro da janela.
            frame.add(bottomPanel, BorderLayout.SOUTH); // Adiciona o painel inferior à parte inferior da janela.

            frame.setVisible(true); // Torna a janela visível.

            adicionarButton.addActionListener(new ActionListener() { // Adiciona um listener ao botão "Adicionar Produto".
                public void actionPerformed(ActionEvent e) {
                    try {
                        String nome = nomeField.getText(); // Obtém o nome do produto.
                        int quantidade = Integer.parseInt(quantidadeField.getText()); // Obtém a quantidade de unidades.

                        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));
                        format.setParseBigDecimal(true);
                        BigDecimal precoEmbalagem = (BigDecimal) format.parse(precoEmbalagemField.getText()); // Obtém o preço da embalagem.
                        BigDecimal porcentagemLucro = (BigDecimal) format.parse(porcentagemField.getText()); // Obtém a porcentagem de lucro.

                        // Adiciona um novo produto à lista de produtos.
                        produtos.add(new Produto(nome, quantidade, precoEmbalagem, porcentagemLucro));

                        // Atualiza a área de texto com a lista de produtos.
                        outputArea.setText("");
                        for (Produto p : produtos) {
                            outputArea.append(p.toString() + "\n");
                        }

                        // Limpa os campos de entrada após adicionar um produto.
                        nomeField.setText("");
                        quantidadeField.setText("");
                        precoEmbalagemField.setText("");
                        porcentagemField.setText("");
                    } catch (NumberFormatException | ParseException ex) {
                        // Exibe uma mensagem de erro se houver problemas com os valores de entrada.
                        JOptionPane.showMessageDialog(frame, "Por favor, insira valores válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        });
    }
}
