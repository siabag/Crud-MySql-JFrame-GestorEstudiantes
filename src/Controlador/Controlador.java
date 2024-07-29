package Controlador;

import Modelo.Estudiante;
import Modelo.EstudianteDAO;
import Vista.vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Controlador implements ActionListener {

    EstudianteDAO dao = new EstudianteDAO();
    Estudiante estudiante = new Estudiante();
    vista vista;
    DefaultTableModel modelo = new DefaultTableModel();
    
    public Controlador(vista v){
        this.vista = v;
        this.vista.btnListar.addActionListener(this);
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnDelete.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnUpdate.addActionListener(this);
    }

    void nuevo() {
        vista.txtId.setText("");
        vista.txtNombre.setText("");
        vista.txtCorreo.setText("");
        vista.txtTelefono.setText("");
        vista.txtId.requestFocus();
    }

    void limpiarTabla() {
        int fila = vista.tabla.getRowCount();
        for (int i = fila-1; i >= 0; i--) {
            modelo.removeRow(i);
        }
    }

    public void delete() {
        int fila = vista.tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una fila");
        } else {
            int id = Integer.parseInt(vista.tabla.getValueAt(fila, 0).toString());
            dao.Delete(id);
            JOptionPane.showMessageDialog(vista, "Usuario eliminado");
        }
        limpiarTabla();
    }

    public void add() {
        String nom = vista.txtNombre.getText();
        String correo = vista.txtCorreo.getText();
        String tel = vista.txtTelefono.getText();

        estudiante.setNom(nom);
        estudiante.setCorreo(correo);
        estudiante.setTelefono(tel);

        int fila = dao.agregar(estudiante);

        if (fila == 1) {
            JOptionPane.showMessageDialog(vista, "Estudiante agregado con exito");
        } else {
            JOptionPane.showMessageDialog(vista, "Error");
        }
        limpiarTabla();
    }

    public void Actualizar() {
        if (vista.txtId.getText().equals("")) {
            JOptionPane.showMessageDialog(vista, "Se sugiere que el usuario"
                    + " seleccione la opción \"Editar\" para solucionar el problema");
        } else {
            int id = Integer.parseInt(vista.txtId.getText());
            String nom = vista.txtNombre.getText();
            String correo = vista.txtCorreo.getText();
            String tel = vista.txtTelefono.getText();

            estudiante.setId(id);
            estudiante.setNom(nom);
            estudiante.setCorreo(correo);
            estudiante.setTelefono(tel);

            int fila = dao.Actualizar(estudiante);

            if (fila == 1) {
                JOptionPane.showMessageDialog(vista, "Estudiante actualizado con exito");
            } else {
                JOptionPane.showMessageDialog(vista, "Error");
            }
            limpiarTabla();
        }
    }
    
    void centrarCeldas(JTable tabla){
        DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer();
        renderizador.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < vista.tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderizador);
        }
    }
    
    public void listar(JTable tabla){
        centrarCeldas(tabla);
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        List<Estudiante> lista = dao.listar();
        Object[] objeto = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            objeto[0] = lista.get(i).getId();
            objeto[1] = lista.get(i).getNom();
            objeto[2] = lista.get(i).getCorreo();
            objeto[3] = lista.get(i).getTelefono();
            modelo.addRow(objeto);
        }
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == vista.btnListar){
            limpiarTabla();
            listar(vista.tabla);
            nuevo();
        }
        if(e.getSource() == vista.btnAgregar){
            if(vista.txtNombre.getText().equals("")||vista.txtTelefono.getText().equals("")||vista.txtCorreo.getText().equals("")){
                JOptionPane.showMessageDialog(vista, "No se permite agregar datos vacios.");
            }else{
                add();
                listar(vista.tabla);
                nuevo();
            }
        }
        if(e.getSource() == vista.btnEditar){
            int fila = vista.tabla.getSelectedRow();
            if(fila == -1){
                JOptionPane.showMessageDialog(vista, "Seleccione una fila");
            }else{
                int id = Integer.parseInt(vista.tabla.getValueAt(fila, 0).toString());
                String nom = vista.tabla.getValueAt(fila, 1).toString();
                String correo = vista.tabla.getValueAt(fila, 2).toString();
                String tel = vista.tabla.getValueAt(fila, 3).toString();
                vista.txtId.setText(""+id);
                vista.txtNombre.setText(nom);
                vista.txtCorreo.setText(correo);
                vista.txtTelefono.setText(tel);
            }
        }
        if(e.getSource() == vista.btnUpdate){
            Actualizar();
            listar(vista.tabla);
            nuevo();
        }
        if(e.getSource() == vista.btnDelete){
            delete();
            listar(vista.tabla);
            nuevo();
        }
    }
}
