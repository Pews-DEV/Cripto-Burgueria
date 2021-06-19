import javax.swing.*;
import java.util.*;
//import javax.swing.Border;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class KriptoBurgueria {
	public JFrame main_frame = new JFrame("Kripto Lanchonete - v1.0");
	public JPanel main_container = new JPanel();
	public Font font_padrao = new Font("Contrail One", Font.PLAIN, 52);
	public Font font_button = new Font("roboto", Font.PLAIN, 14);
	public Cardapio cardapio;
	public Historico historico;
	private Banco banco = new Banco();

	public void setCardapio(Cardapio cardapio){
		this.cardapio = cardapio;
	}
	public void setHistorico(Historico historico){
		this.historico = historico; 
	}
	public static void main(String[] args) {
		KriptoBurgueria app = new KriptoBurgueria();
		app.setCardapio(new Cardapio(app.main_frame, app.font_button, app.main_container));
		app.setHistorico(new Historico(app.main_frame, app.font_button, app.main_container));
		app.configure();
		app.tela_principal();
	}

	public void configure() {
		this.banco.start();
		this.main_frame.setLayout(null);
		this.main_frame.setSize(720, 512);
		this.main_frame.setVisible(true);
		this.main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.main_frame.getContentPane().setBackground(Color.decode("#212F4D"));
	}

	public void adicionar_pedido(HashMap<String, ArrayList<String>> pedido){
		banco.adicionar_pedido(pedido);
	}

	public void tela_principal() {

		this.main_frame.add(this.main_container);
		this.main_container.setBackground(Color.decode("#212F4D"));
		this.main_container.setVisible(true);
		this.main_container.setLayout(null);
		this.main_container.setSize(720, 512);

		JLabel kripto = new JLabel("KRIPTO");
		kripto.setBounds(290, 79, 315, 150);
		kripto.setForeground(Color.decode("#ebf1fb"));
		kripto.setFont(this.font_padrao);
		this.main_container.add(kripto);

		JLabel burgueria = new JLabel("BURGUERIA");
		burgueria.setBounds(250, 141, 315, 150);
		burgueria.setForeground(Color.decode("#ebf1fb"));
		burgueria.setFont(this.font_padrao);
		this.main_container.add(burgueria);

		JButton pedido = new JButton("Faça seu pedido");
		pedido.setBounds(262, 281, 195, 67);
		pedido.setBackground(Color.decode("#3A8AD3"));
		pedido.setForeground(Color.decode("#FFFFFF"));
		pedido.setFont(this.font_button);
		this.main_container.add(pedido);

		JButton historico_bt = new JButton("Historico de pedidos");
		historico_bt.setBounds(262, 351, 195, 20);
		historico_bt.setForeground(Color.decode("#FFFFFF"));
		historico_bt.setBackground(Color.decode("#212F4D"));
		historico_bt.setFont(this.font_button);
		this.main_container.add(historico_bt);

		historico_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				historico.ultima_tela.setVisible(false);
				historico.main_container.setVisible(true);
				historico.atualizarPedidos();
			}
		});

		pedido.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// setCardapio(new Cardapio(main_frame, font_button, main_container));
				main_container.setVisible(false);
				cardapio.main_container.setVisible(true);

				JButton finalizar = new JButton("Finalizar");
				finalizar.setBounds(360, 435, 97, 40);
				finalizar.setBackground(Color.decode("#28a745"));
				finalizar.setForeground(Color.decode("#FFFFFF"));
				finalizar.setFont(font_button);
				
				cardapio.setFinalizar(finalizar);
				finalizar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0){
						if(cardapio.test_has_pedidos()){
							adicionar_pedido(cardapio.detalhamento_pedido);
							cardapio.reset();
							cardapio.valor_total = 0f;
							cardapio.iniciar_pedido();
							cardapio.main_container.setVisible(false);
							main_container.setVisible(true);
							JOptionPane.showMessageDialog(main_container, "Pedido aceito com sucesso", "Pedidos", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(cardapio.main_container, "É necessário escolher ao menos um ingrediente", "Pedidos", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
			}
		});

	}

}
