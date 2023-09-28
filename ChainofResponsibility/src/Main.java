import java.util.Scanner;

class Pedido { private String nomeCliente; private double valorPedido; private String metodoPagamento;
    public Pedido(String nomeCliente, double valorPedido, String metodoPagamento) {
        this.nomeCliente = nomeCliente;
        this.valorPedido = valorPedido;
        this.metodoPagamento = metodoPagamento;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public double getValorPedido() {
        return valorPedido;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public boolean isAutenticado() {
        // Simulação de autenticação (pode ser implementada de acordo com as necessidades)
        return true;
    }

    public boolean isPagamentoValido() {
        // Simulação de validação de pagamento (pode ser implementada de acordo com as necessidades)
        return true;
    }

    public boolean isEstoqueDisponivel() {
        // Simulação de verificação de estoque (pode ser implementada de acordo com as necessidades)
        return true;
    }
}

interface Handler {
    void processarPedido(Pedido pedido);
}

class AutenticacaoHandler implements Handler {
    private Handler proximoHandler;

    public AutenticacaoHandler(Handler proximoHandler) {
        this.proximoHandler = proximoHandler;
    }

    @Override
    public void processarPedido(Pedido pedido) {
        if (pedido.isAutenticado()) {
            System.out.println("Autenticação bem-sucedida.");
            proximoHandler.processarPedido(pedido);
        } else {
            System.out.println("Autenticação falhou. Pedido interrompido.");
        }
    }
}

class ValidacaoPagamentoHandler implements Handler {
    private Handler proximoHandler;

    public ValidacaoPagamentoHandler(Handler proximoHandler) {
        this.proximoHandler = proximoHandler;
    }

    @Override
    public void processarPedido(Pedido pedido) {
        if (pedido.isPagamentoValido()) {
            System.out.println("Pagamento validado com sucesso.");
            proximoHandler.processarPedido(pedido);
        } else {
            System.out.println("Pagamento inválido. Pedido interrompido.");
        }
    }
}

class VerificacaoEstoqueHandler implements Handler {
    private Handler proximoHandler;

    public VerificacaoEstoqueHandler(Handler proximoHandler) {
        this.proximoHandler = proximoHandler;
    }

    @Override
    public void processarPedido(Pedido pedido) {
        if (pedido.isEstoqueDisponivel()) {
            System.out.println("Estoque verificado com sucesso.");
            proximoHandler.processarPedido(pedido);
        } else {
            System.out.println("Estoque insuficiente. Pedido interrompido.");
        }
    }
}

class GeracaoNotaFiscalHandler implements Handler {
    @Override
    public void processarPedido(Pedido pedido) {
        System.out.println("Nota fiscal gerada com sucesso.");
    }
}

class Cliente {
    private String nome;

    public Cliente(String nome) {
        this.nome = nome;
    }

    public Pedido fazerPedido() {
        Scanner scanner = new Scanner(System.in);
        double valorPedido = 0;
        String metodoPagamento = "";

        // Validação da entrada do valor do pedido
        boolean entradaValida = false;
        while (!entradaValida) {
            try {
                System.out.print("Digite o valor do pedido: ");
                valorPedido = Double.parseDouble(scanner.nextLine());
                entradaValida = true;
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número válido.");
            }
        }

        // Validação da entrada do método de pagamento
        entradaValida = false;
        while (!entradaValida) {
            System.out.print("Digite o método de pagamento (crédito, débito, etc.): ");
            metodoPagamento = scanner.nextLine().trim().toLowerCase();
            if (metodoPagamento.isEmpty()) {
                System.out.println("Método de pagamento não pode estar vazio.");
            } else {
                entradaValida = true;
            }
        }

        return new Pedido(nome, valorPedido, metodoPagamento);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome do cliente: ");
        String nomeCliente = scanner.nextLine();

        Cliente cliente = new Cliente(nomeCliente);

        Handler geracaoNotaFiscalHandler = new GeracaoNotaFiscalHandler();
        Handler verificacaoEstoqueHandler = new VerificacaoEstoqueHandler(geracaoNotaFiscalHandler);
        Handler validacaoPagamentoHandler = new ValidacaoPagamentoHandler(verificacaoEstoqueHandler);
        Handler autenticacaoHandler = new AutenticacaoHandler(validacaoPagamentoHandler);

        Pedido pedido = cliente.fazerPedido();

        autenticacaoHandler.processarPedido(pedido);
    }
}
