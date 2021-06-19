import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Cardapio extends Janela{
    public String[] setores = {"Pães", "Queijos", "Carnes", "Verduras", "Molhos", "Outros"};
    public Float valor_total = 0f;
    public boolean _retorno = false;

    public Integer contador;
    public JButton finalizar;
    public JLabel total;

    public HashMap<String, ArrayList<Integer>> pedido = new HashMap<String, ArrayList<Integer>>();
    public HashMap<String, ArrayList<String>> detalhamento_pedido = new HashMap<String, ArrayList<String>>();


    public Cardapio(JFrame main_frame, Font font_button, JPanel ultima_tela) {
        super(main_frame, font_button, ultima_tela);
        
        // Iniciando valores do pedido
        this.iniciar_pedido();
        this.configure();
        this.main_container.setVisible(false);
        // Adicionando botões principais a tela
        this.gerar_botoes();
    }

    public void setFinalizar(JButton finalizar){
        this.main_container.add(finalizar);
    }

    public void buttonAction(String setor){
        ArrayList<Integer> quantidades = this.pedido.get(setor); 

        // Criando botão usado para finalizar os pedidos
        JButton finalizar_ingredientes = new JButton("Pronto");
        finalizar_ingredientes.setBounds(360, 435, 97, 40);
        finalizar_ingredientes.setBackground(Color.decode("#28a745"));
        finalizar_ingredientes.setForeground(Color.decode("#FFFFFF"));
        finalizar_ingredientes.setFont(this.font_button);


        Ingredientes nova_tela = new Ingredientes(
            this.main_frame, 
            this.font_button,
            this.main_container, 
            setor, 
            quantidades,
            finalizar_ingredientes);
        
        finalizar_ingredientes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0){
                main_container.setVisible(true);
                nova_tela.main_container.setVisible(false);

                // Trazendo ingredientes escolhidos na aba de ingredientes
                ArrayList<Integer> novas_quantidades = new ArrayList<Integer>(List.of(0, 0, 0, 0, 0 ,0 ,0 ,0 ,0));
                contador = 0;
                nova_tela.campos.forEach((item) -> {
                    Integer quantidade_item = Integer.parseInt(item.getValue().toString());
                    novas_quantidades.set(contador, quantidade_item);
                    
                    if (nova_tela.options.get(setor).size() > contador){
                        String _quantidade_item = quantidade_item.toString();
                        String price_item = nova_tela.precos.get(setor).get(contador).toString();
                        String name_item = nova_tela.options.get(setor).get(contador).toString();
                        if (quantidade_item > 0){
                            detalhamento_pedido.put(name_item, new ArrayList<>(List.of(price_item, _quantidade_item)));
                        }
                    }

                    contador ++;
                });

                // Setando ingredientes escolhidos
                pedido.put(setor, novas_quantidades);
                calculaValorTotal();
            }
        });

        nova_tela.start();

    }

    public void gerar_botoes(){

        this.total = new JLabel("Total: R$ " + String.format("%.2f", this.valor_total));
		this.total.setBounds(580, 0, 150, 50);
		this.total.setForeground(Color.decode("#ebf1fb"));
		this.main_container.add(total);

        for (int i = 0; i < 6; i++){
            JButton button = new JButton(setores[i]);
            button.setBounds(262, 15 + (70 * i), 195, 50);
            button.setBackground(Color.decode("#3A8AD3"));
            button.setForeground(Color.decode("#FFFFFF"));
            button.setFont(this.font_button);
            this.main_container.add(button);

            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0){
                    buttonAction(arg0.getActionCommand());
                    
                }
            });
        }
    }

    public void reset(){
        pedido = new HashMap<String, ArrayList<Integer>>();
        detalhamento_pedido = new HashMap<String, ArrayList<String>>();
        _retorno = false;
        this.valor_total = 0f;
        calculaValorTotal();
    }

    public void iniciar_pedido(){
        for (String item: this.setores){
            pedido.put(item, new ArrayList<Integer>(List.of(0, 0, 0, 0, 0, 0, 0, 0)));
        };
    }

    private void calculaValorTotal(){
        this.valor_total = 0f;

        for (String key: detalhamento_pedido.keySet()){
            ArrayList<String> precos_setor = detalhamento_pedido.get(key);

            Float valor = Float.parseFloat(precos_setor.get(0).toString());
            Integer quantidade = Integer.parseInt(precos_setor.get(1).toString());
            
            this.valor_total = this.valor_total + (valor * quantidade);
        }

        this.total.setText("Total: R$ " + String.format("%.2f", this.valor_total));
    }

    public boolean test_has_pedidos(){
        for (int i = 0; i < 6; i++){
            ArrayList<Integer> options = pedido.get(setores[i]);
            options.forEach((item) -> {
                if(item > 0){
                    _retorno = true;
                } else if (item < 0){
                    _retorno = false;
                }
            });
        }
        return _retorno;
    }

}