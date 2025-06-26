package interfac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import btree.*;
import graph.*;
import estructura.*;

public class MainFrame extends JFrame {
    private GraphLink<String> grafo;
    private estructura.HashMap<String, BTree<Producto>> arbolesPorPasillo = new estructura.HashMap<>();
    private JTextArea areaEstado;
    private DefaultListModel<String> modeloProductos;
    private JList<String> listaProductos;
    private JComboBox<String> comboOrigen, comboDestino;
    private JPanel panelCentro;
    private GraphPanel panelGrafo;
    private boolean mostrandoArbol = false;

    public MainFrame() {
        super("Sistema de Gestión y Optimización de Inventarios en Almacenes");
        grafo = new GraphLink<>();

        // --- ESTILO MODERNO OSCURO ---
        Color fondoOscuro = new Color(36, 37, 42);
        Color panelOscuro = new Color(44, 45, 51);
        Color panelLateral = new Color(32, 33, 38);
        Color acento = new Color(120, 99, 255);
        Color acentoClaro = new Color(180, 140, 255);
        Color textoClaro = new Color(230, 230, 230);

        // --- FUENTES MODERNAS ---
        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 28);
        Font fuenteDashboard = new Font("Segoe UI", Font.BOLD, 22);
        Font fuenteBoton = new Font("Segoe UI", Font.BOLD, 15);
        Font fuenteLabel = new Font("Segoe UI", Font.PLAIN, 15);
        Font fuenteLista = new Font("Segoe UI", Font.PLAIN, 15);

        setPreferredSize(new Dimension(1200, 800));
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(fondoOscuro);

        // --- MENÚ SUPERIOR RÁPIDO ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(panelOscuro);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título DASHBOARD
        JLabel lblDashboard = new JLabel("DashBoard");
        lblDashboard.setFont(fuenteTitulo);
        lblDashboard.setForeground(textoClaro);

        // Usuario a la derecha
        JLabel lblUsuario = new JLabel("Usuario :");
        lblUsuario.setFont(fuenteLabel);
        lblUsuario.setForeground(textoClaro);
        JLabel lblAdmin = new JLabel("Admin");
        lblAdmin.setFont(fuenteLabel);
        lblAdmin.setForeground(textoClaro);

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.add(lblDashboard, BorderLayout.CENTER);

        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelUsuario.setOpaque(false);
        panelUsuario.add(lblUsuario);
        panelUsuario.add(lblAdmin);

        panelTitulo.add(panelUsuario, BorderLayout.EAST);

        // Panel de controles
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelControles.setOpaque(false);

        comboOrigen = new JComboBox<>();
        comboDestino = new JComboBox<>();
        comboOrigen.setFont(fuenteLabel);
        comboDestino.setFont(fuenteLabel);

        JButton btnBuscarRuta = new JButton("Buscar Ruta Óptima");
        JButton btnCerrarVia = new JButton("Cerrar Vía");
        JButton btnInsertarCamino = new JButton("Insertar Camino");
        JButton btnModificarPeso = new JButton("Modificar Peso");
        JButton btnAgregarPasillo = new JButton("Agregar Pasillo");
        JButton btnEliminarPasillo = new JButton("Eliminar Nodo");
        JButton btnAnimarRuta = new JButton("Animar Ruta Óptima");
        JButton[] botones = {btnBuscarRuta, btnCerrarVia, btnInsertarCamino, btnModificarPeso, btnAgregarPasillo, btnEliminarPasillo, btnAnimarRuta};
        for (JButton b : botones) {
            b.setFont(fuenteBoton);
            b.setBackground(acento);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
            b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        }
        comboOrigen.setBackground(panelOscuro);
        comboDestino.setBackground(panelOscuro);
        comboOrigen.setForeground(textoClaro);
        comboDestino.setForeground(textoClaro);

        JLabel lblOrigen = new JLabel("Pasillo Origen:");
        JLabel lblDestino = new JLabel("Pasillo Destino:");
        lblOrigen.setFont(fuenteLabel);
        lblDestino.setFont(fuenteLabel);
        lblOrigen.setForeground(textoClaro);
        lblDestino.setForeground(textoClaro);

        panelControles.add(lblOrigen);
        panelControles.add(comboOrigen);
        panelControles.add(lblDestino);
        panelControles.add(comboDestino);
        panelControles.add(btnBuscarRuta);
        panelControles.add(btnCerrarVia);
        panelControles.add(btnInsertarCamino);
        panelControles.add(btnModificarPeso);
        panelControles.add(btnAgregarPasillo);
        panelControles.add(btnEliminarPasillo);
        panelControles.add(btnAnimarRuta);

        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelControles, BorderLayout.SOUTH);

        // --- PANEL IZQUIERDO: ACCIONES GRAFO ---
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(panelLateral);
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        JButton btnBFS = new JButton("Explorar BFS");
        JButton btnDFS = new JButton("Explorar DFS");
        JButton btnCiclos = new JButton("Detectar Ciclos");
        JButton btnComponentes = new JButton("Componentes Conexas");
        JButton btnZonasAisladas = new JButton("Zonas Aisladas");
        JButton[] botonesLateral = {btnBFS, btnDFS, btnCiclos, btnComponentes, btnZonasAisladas};
        for (JButton b : botonesLateral) {
            b.setFont(fuenteBoton);
            b.setBackground(panelOscuro);
            b.setForeground(textoClaro);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        }
        panelIzquierdo.add(btnBFS);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnDFS);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnCiclos);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnComponentes);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnZonasAisladas);

        // --- PANEL CENTRAL: GRAFO VISUAL ---
        panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(fondoOscuro);
        panelGrafo = new GraphPanel();
        panelCentro.add(panelGrafo, BorderLayout.CENTER);

        // --- PANEL DERECHO: PRODUCTOS ---
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(panelLateral);
        modeloProductos = new DefaultListModel<>();
        listaProductos = new JList<>(modeloProductos);
        listaProductos.setFont(fuenteLista);
        listaProductos.setBackground(panelOscuro);
        listaProductos.setForeground(textoClaro);
        listaProductos.setSelectionBackground(acentoClaro);
        listaProductos.setSelectionForeground(Color.BLACK);
        JButton btnAgregarProducto = new JButton("Añadir Producto");
        JButton btnModificarProducto = new JButton("Modificar Producto");
        JButton btnVerArbol = new JButton("Ver Árbol B+");
        JButton btnVerGrafo = new JButton("Ver Grafo");
        JButton[] botonesProd = {btnAgregarProducto, btnModificarProducto, btnVerArbol, btnVerGrafo};
        for (JButton b : botonesProd) {
            b.setFont(fuenteBoton);
            b.setBackground(acento);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        }
        JPanel panelProdBtns = new JPanel(new GridLayout(4, 1, 5, 5));
        panelProdBtns.setBackground(panelLateral);
        panelProdBtns.add(btnAgregarProducto);
        panelProdBtns.add(btnModificarProducto);
        panelProdBtns.add(btnVerArbol);
        panelProdBtns.add(btnVerGrafo);
        JLabel lblProductos = new JLabel("Productos:");
        lblProductos.setFont(fuenteDashboard);
        lblProductos.setForeground(textoClaro);
        panelDerecho.add(lblProductos, BorderLayout.NORTH);
        panelDerecho.add(new JScrollPane(listaProductos), BorderLayout.CENTER);
        panelDerecho.add(panelProdBtns, BorderLayout.SOUTH);

        // --- PANEL MENSAJES ABAJO ---
        areaEstado = new JTextArea(2, 80);
        areaEstado.setEditable(false);
        areaEstado.setFont(fuenteDashboard);
        areaEstado.setForeground(acento);
        areaEstado.setBackground(panelOscuro);
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(panelOscuro);
        JLabel lblEstado = new JLabel("Ruta Óptima / Resultados");
        lblEstado.setFont(fuenteDashboard);
        lblEstado.setForeground(acentoClaro);
        panelEstado.add(lblEstado, BorderLayout.WEST);
        panelEstado.add(areaEstado, BorderLayout.CENTER);

        // --- AÑADIR PANELES AL FRAME ---
        add(panelSuperior, BorderLayout.NORTH);
        add(panelIzquierdo, BorderLayout.WEST);
        add(panelCentro, BorderLayout.CENTER);
        add(panelDerecho, BorderLayout.EAST);
        add(panelEstado, BorderLayout.SOUTH);

        // --- ACCIONES DE MENÚ SUPERIOR ---
        btnAgregarPasillo.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del pasillo:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                grafo.insertVertex(nombre.trim());
                arbolesPorPasillo.put(nombre.trim(), new BTree<>(4));
                actualizarPasillos();
                mostrarEstado("Pasillo ingresado: " + nombre);
                panelGrafo.repaint();
            }   
        });
        btnEliminarPasillo.addActionListener(e -> {
            Object[] pasillos = getPasillos();
            if (pasillos.length == 0) {
                mostrarEstado("No hay pasillos para eliminar.");
                return;
            }
            String nombre = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el pasillo a eliminar:",
                "Eliminar Nodo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                pasillos,
                pasillos[0]
            );
            if (nombre != null) {
                grafo.removeVertex(nombre);
                arbolesPorPasillo.remove(nombre);
                actualizarPasillos();
                mostrarEstado("Pasillo eliminado: " + nombre);
                panelGrafo.repaint();
            }
        });
        btnInsertarCamino.addActionListener(e -> {
            String origen = (String) comboOrigen.getSelectedItem();
            String destino = (String) comboDestino.getSelectedItem();
            String pesoStr = JOptionPane.showInputDialog(this, "Peso del camino:");
            if (origen == null || destino == null || origen.equals(destino)) {
                mostrarEstado("Seleccione origen y destino distintos.");
                return;
            }
            try {
                int peso = Integer.parseInt(pesoStr);
                grafo.insertEdge(origen, destino, peso);
                mostrarEstado("Camino agregado: " + origen + " -> " + destino + " (peso " + peso + ")");
                panelGrafo.repaint();
            } catch (Exception ex) {
                mostrarEstado("Peso inválido.");
            }
        });
        btnModificarPeso.addActionListener(e -> {
            String origen = (String) comboOrigen.getSelectedItem();
            String destino = (String) comboDestino.getSelectedItem();
            String pesoStr = JOptionPane.showInputDialog(this, "Nuevo peso:");
            if (origen == null || destino == null || origen.equals(destino)) {
                mostrarEstado("Seleccione origen y destino distintos.");
                return;
            }
            try {
                int peso = Integer.parseInt(pesoStr);
                grafo.modifyEdgeWeight(origen, destino, peso);
                mostrarEstado("Peso modificado: " + origen + " -> " + destino + " (nuevo peso " + peso + ")");
                panelGrafo.repaint();
            } catch (Exception ex) {
                mostrarEstado("Peso inválido.");
            }
        });
        btnCerrarVia.addActionListener(e -> {
            String origen = (String) comboOrigen.getSelectedItem();
            String destino = (String) comboDestino.getSelectedItem();
            if (origen == null || destino == null || origen.equals(destino)) {
                mostrarEstado("Seleccione origen y destino distintos.");
                return;
            }
            grafo.removeEdge(origen, destino);
            mostrarEstado("Vía cerrada entre: " + origen + " y " + destino);
            panelGrafo.repaint();
        });
        btnBuscarRuta.addActionListener(e -> {
            String origen = (String) comboOrigen.getSelectedItem();
            String destino = (String) comboDestino.getSelectedItem();
            if (origen == null || destino == null || origen.equals(destino)) {
                mostrarEstado("Seleccione origen y destino distintos.");
                return;
            }
            ArrayList<String> ruta = grafo.dijkstraPath(origen, destino);
            if (ruta == null || ruta.isEmpty()) {
                mostrarEstado("No hay ruta conectada.");
            } else {
                JFrame frameAnim = new JFrame("Animación Ruta Óptima");
                AnimacionRutaPanel panelAnim = new AnimacionRutaPanel(grafo, ruta);
                frameAnim.add(panelAnim);
                frameAnim.setSize(900, 600);
                frameAnim.setLocationRelativeTo(this);
                frameAnim.setVisible(true);
                panelAnim.iniciarAnimacion();
            }
        });

        btnAnimarRuta.addActionListener(e -> {
    String origen = (String) comboOrigen.getSelectedItem();
    String destino = (String) comboDestino.getSelectedItem();
    if (origen == null || destino == null || origen.equals(destino)) {
        mostrarEstado("Seleccione origen y destino distintos.");
        return;
    }

    ArrayList<String> rutaList = grafo.dijkstraPath(origen, destino);
    if (rutaList == null || rutaList.isEmpty()) {
        mostrarEstado("No hay ruta conectada.");
        return;
    }

    // Crear ventana de animación
    JFrame frameAnim = new JFrame("Animación Ruta Óptima");
    frameAnim.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    // Panel animado (asegúrate de que AnimacionRutaPanel tenga lógica para animar paso a paso)
    AnimacionRutaPanel panelAnim = new AnimacionRutaPanel(grafo, rutaList);
    frameAnim.add(panelAnim);
    frameAnim.setSize(900, 600);

    // Asegúrate de reemplazar 'this' por tu ventana principal si aplica
    frameAnim.setLocationRelativeTo(null); // o framePrincipal si lo tienes
    frameAnim.setVisible(true);

    // Iniciar animación
    panelAnim.iniciarAnimacion();
});


        // --- ACCIONES PRODUCTOS ---
        btnAgregarProducto.addActionListener(e -> {
            Object[] pasillos = getPasillos();
            if (pasillos.length == 0) {
                mostrarEstado("No hay pasillos para agregar productos.");
                return;
            }
            String pasillo = (String) JOptionPane.showInputDialog(this, "Seleccione el pasillo:", "Pasillo", JOptionPane.QUESTION_MESSAGE, null, pasillos, pasillos[0]);
            if (pasillo == null) return;
            JTextField id = new JTextField();
            JTextField nombre = new JTextField();
            JTextField precio = new JTextField();
            JTextField cantidad = new JTextField();
            JTextField demanda = new JTextField();
            JPanel panel = new JPanel(new GridLayout(0,2));
            panel.add(new JLabel("ID:")); panel.add(id);
            panel.add(new JLabel("Nombre:")); panel.add(nombre);
            panel.add(new JLabel("Precio:")); panel.add(precio);
            panel.add(new JLabel("Cantidad:")); panel.add(cantidad);
            panel.add(new JLabel("Demanda:")); panel.add(demanda);
            int res = JOptionPane.showConfirmDialog(this, panel, "Añadir Producto", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    Producto prod = new Producto(
                        id.getText().trim(),
                        nombre.getText().trim(),
                        Double.parseDouble(precio.getText().trim()),
                        Integer.parseInt(cantidad.getText().trim()),
                        Integer.parseInt(demanda.getText().trim())
                    );
                    arbolesPorPasillo.get(pasillo).insert(prod);
                    actualizarProductos(pasillo);
                    mostrarEstado("Producto agregado en " + pasillo + ": " + prod.getId());
                } catch (Exception ex) {
                    mostrarEstado("Datos inválidos: " + ex.getMessage());
                }
            }
        });

        btnModificarProducto.addActionListener(e -> {
            Object[] pasillos = getPasillos();
            if (pasillos.length == 0) {
                mostrarEstado("No hay pasillos.");
                return;
            }
            String pasillo = (String) JOptionPane.showInputDialog(this, "Seleccione el pasillo:", "Pasillo", JOptionPane.QUESTION_MESSAGE, null, pasillos, pasillos[0]);
            if (pasillo == null) return;
            String codigo = JOptionPane.showInputDialog(this, "Ingrese el código del producto:");
            if (codigo == null || codigo.trim().isEmpty()) return;
            Producto prod = arbolesPorPasillo.get(pasillo).searchById(codigo.trim());
            if (prod == null) {
                mostrarEstado("No se encontró el producto en " + pasillo + ".");
                return;
            }
            JTextField nombre = new JTextField(prod.getNombre());
            JTextField precio = new JTextField(String.valueOf(prod.getPrecio()));
            JTextField cantidad = new JTextField(String.valueOf(prod.getCantidad()));
            JTextField demanda = new JTextField(String.valueOf(prod.getDemanda()));
            JPanel panel = new JPanel(new GridLayout(0,2));
            panel.add(new JLabel("Nombre:")); panel.add(nombre);
            panel.add(new JLabel("Precio:")); panel.add(precio);
            panel.add(new JLabel("Cantidad:")); panel.add(cantidad);
            panel.add(new JLabel("Demanda:")); panel.add(demanda);
            int res = JOptionPane.showConfirmDialog(this, panel, "Modificar Producto", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    prod.setNombre(nombre.getText().trim());
                    prod.setPrecio(Double.parseDouble(precio.getText().trim()));
                    prod.setCantidad(Integer.parseInt(cantidad.getText().trim()));
                    prod.setDemanda(Integer.parseInt(demanda.getText().trim()));
                    actualizarProductos(pasillo);
                    mostrarEstado("Producto modificado en " + pasillo + ": " + prod.getId());
                } catch (Exception ex) {
                    mostrarEstado("Datos inválidos: " + ex.getMessage());
                }
            }
        });

        btnVerArbol.addActionListener(e -> {
            Object[] pasillos = getPasillos();
            if (pasillos.length == 0) {
                mostrarEstado("No hay pasillos.");
                return;
            }
            String pasillo = (String) JOptionPane.showInputDialog(this, "Seleccione el pasillo:", "Ver Árbol B+", JOptionPane.QUESTION_MESSAGE, null, pasillos, pasillos[0]);
            if (pasillo == null) return;
            BTree<Producto> arbol = arbolesPorPasillo.get(pasillo);
            JFrame frameArbol = new JFrame("Árbol B+ de " + pasillo);
            frameArbol.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameArbol.add(new BTreeDrawPanel(arbol));
            frameArbol.setSize(900, 600);
            frameArbol.setLocationRelativeTo(this);
            frameArbol.setVisible(true);
        });

        btnVerGrafo.addActionListener(e -> {
            mostrandoArbol = false;
            actualizarPanelCentro();
        });

        // --- ACCIONES GRAFO (izquierda) ---
        btnBFS.addActionListener(e -> {
    String origen = (String) comboOrigen.getSelectedItem();
    String destino = (String) comboDestino.getSelectedItem(); // <- NUEVO

    if (origen == null || destino == null) {
        mostrarEstado("Seleccione un distrito de origen y destino.");
        return;
    }

    estructura.ArrayList<String> recorrido = grafo.bfsPath(origen, destino); // <- CORREGIDO

    if (recorrido == null || recorrido.size() == 0) {
        mostrarEstado("No hay camino BFS entre los distritos seleccionados.");
    } else {
        StringBuilder sb = new StringBuilder("Camino BFS: ");
        for (int i = 0; i < recorrido.size(); i++) {
            sb.append(recorrido.get(i));
            if (i < recorrido.size() - 1) sb.append(" -> ");
        }
        mostrarEstado(sb.toString());
        panelGrafo.setRutaIluminada(recorrido);
        panelGrafo.repaint();
    }
});


        btnDFS.addActionListener(e -> {
    String origen = (String) comboOrigen.getSelectedItem();
    if (origen == null) {
        mostrarEstado("Seleccione un distrito de origen.");
        return;
    }

    estructura.ArrayList<String> recorrido = grafo.dfs(origen); // <-- ahora devuelve lista

    if (recorrido == null || recorrido.size() == 0) {
        mostrarEstado("No hay recorrido DFS.");
    } else {
        StringBuilder sb = new StringBuilder("Recorrido DFS: ");
        for (int i = 0; i < recorrido.size(); i++) {
            sb.append(recorrido.get(i));
            if (i < recorrido.size() - 1) sb.append(" -> ");
        }
        mostrarEstado(sb.toString());
        panelGrafo.setRutaIluminada(recorrido);
        panelGrafo.repaint();
    }
});


        btnCiclos.addActionListener(e -> {
            boolean tieneCiclo = grafo.esCiclo();
            if (tieneCiclo) {
                mostrarEstado("El grafo contiene al menos un ciclo.");
            } else {
                mostrarEstado("El grafo NO contiene ciclos.");
            }
        });

        btnComponentes.addActionListener(e -> {
            boolean componentes = grafo.isConexo();
            if (componentes){
                mostrarEstado("El grafo es conexo.");
            } else {
                mostrarEstado("El grafo NO es conexo.");
            }
        });

        btnZonasAisladas.addActionListener(e -> {
            ArrayList<String> aislados = grafo.zonasAisladas();
            if (aislados == null || aislados.size() == 0) {
                mostrarEstado("No hay zonas aisladas.");
            } else {
                StringBuilder sb = new StringBuilder("Zonas aisladas: ");
                for (int i = 0; i < aislados.size(); i++) {
                    sb.append(aislados.get(i));
                    if (i < aislados.size() - 1) sb.append(", ");
                }
                mostrarEstado(sb.toString());
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        actualizarPasillos();
    }

    private void actualizarPanelCentro() {
        panelCentro.removeAll();
        panelCentro.add(panelGrafo, BorderLayout.CENTER);
        panelCentro.revalidate();
        panelCentro.repaint();
    }

    private void actualizarPasillos() {
        comboOrigen.removeAllItems();
        comboDestino.removeAllItems();
        Node<Vertex<String>> aux = grafo.listVertex.getHead();
        while (aux != null) {
            comboOrigen.addItem(aux.data.getData());
            comboDestino.addItem(aux.data.getData());
            aux = aux.next;
        }
    }

    private void actualizarProductos(String pasillo) {
        modeloProductos.clear();
        BTree<Producto> arbol = arbolesPorPasillo.get(pasillo);
        if (arbol != null) listarProductos(arbol.root);
    }

    private void listarProductos(NodeBTree<Producto> nodo) {
        // Recorrer todas las hojas en línea usando next (B+)
        if (nodo == null) return;
        // Ir hasta la hoja más a la izquierda
        while (!nodo.isLeaf) {
            nodo = nodo.childs.get(0);
        }
        // Recorrer hojas usando next
        while (nodo != null) {
            for (int i = 0; i < nodo.count; i++) {
                Producto p = nodo.keys.get(i);
                if (p != null) modeloProductos.addElement(p.getId());
            }
            nodo = nodo.next;
        }
    }

    private void mostrarEstado(String msg) {
        areaEstado.setText(msg);
    }

    private Object[] getPasillos() {
        estructura.ArrayList<String> keys = new estructura.ArrayList<>();
        Node<Vertex<String>> aux = grafo.listVertex.getHead();
        while (aux != null) {
            keys.add(aux.data.getData());
            aux = aux.next;
        }
        Object[] arr = new Object[keys.size()];
        for (int i = 0; i < keys.size(); i++) arr[i] = keys.get(i);
        return arr;
    }

    // --- PANEL CENTRAL: GRAFO VISUAL CON FLECHAS Y PONDERACIÓN ---
    class GraphPanel extends JPanel {
        private java.util.List<String> rutaIluminada = null;
        @SuppressWarnings("unchecked")
        public void setRutaIluminada(estructura.ArrayList<String> rutaList) {
            this.rutaIluminada = (java.util.List<String>) rutaList;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            java.util.HashMap<String, Point> posiciones = new java.util.HashMap<>();
            int total = 0;
            Node<Vertex<String>> aux = grafo.listVertex.getHead();
            while (aux != null) { total++; aux = aux.next; }
            double angleStep = total > 0 ? 2 * Math.PI / total : 0;
            int radius = 250;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            aux = grafo.listVertex.getHead();
            int count = 0;
            while (aux != null) {
                double angle = count * angleStep;
                int nx = (int) (centerX + radius * Math.cos(angle));
                int ny = (int) (centerY + radius * Math.sin(angle));
                posiciones.put(aux.data.getData(), new Point(nx, ny));
                count++;
                aux = aux.next;
            }

            // Dibuja aristas con flechas y ponderación
            aux = grafo.listVertex.getHead();
            while (aux != null) {
                String origen = aux.data.getData();
                Point p1 = posiciones.get(origen);
                Node<Edge<String>> ady = aux.data.getListAdj().getHead();
                while (ady != null) {
                    String destino = ady.data.getDestination().getData();
                    Point p2 = posiciones.get(destino);
                    if (p1 != null && p2 != null) {
                        boolean ilum = false;
                        if (rutaIluminada != null) {
                            for (int i = 0; i < rutaIluminada.size() - 1; i++) {
                                if (rutaIluminada.get(i).equals(origen) && rutaIluminada.get(i+1).equals(destino)) {
                                    ilum = true;
                                    break;
                                }
                            }
                        }
                        g.setColor(ilum ? Color.RED : Color.BLUE);
                        drawArrow(g, p1.x, p1.y, p2.x, p2.y);
                        String peso = String.valueOf(ady.data.getWeight());
                        int px = (p1.x + p2.x) / 2;
                        int py = (p1.y + p2.y) / 2;
                        g.setColor(Color.BLACK);
                        g.drawString(peso, px, py);
                    }
                    ady = ady.next;
                }
                aux = aux.next;
            }

            // Dibuja nodos
            for (java.util.Map.Entry<String, Point> entry : posiciones.entrySet()) {
                Point p = entry.getValue();
                g.setColor(new Color(255, 170, 0));
                g.fillOval(p.x - 50, p.y - 20, 100, 40);
                g.setColor(Color.BLACK);
                g.drawOval(p.x - 50, p.y - 20, 100, 40);
                g.drawString(entry.getKey(), p.x - 30, p.y + 5);
            }
        }
        private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
            Graphics2D g = (Graphics2D) g1;
            g.setStroke(new BasicStroke(2));
            g.drawLine(x1, y1, x2, y2);
            double phi = Math.toRadians(30);
            int barb = 15;
            double dy = y2 - y1, dx = x2 - x1;
            double theta = Math.atan2(dy, dx);
            double x, y;
            for (int j = 0; j < 2; j++) {
                double rho = theta + phi - j * 2 * phi;
                x = x2 - barb * Math.cos(rho);
                y = y2 - barb * Math.sin(rho);
                g.drawLine(x2, y2, (int) x, (int) y);
            }
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(700, 600);
        }
    }

    // --- PANEL PARA DIBUJAR ÁRBOL B+ ESTILO IMAGEN ---
    class BTreeDrawPanel extends JPanel {
        private static final int NODO_ANCHO_MIN = 48;
        private static final int NODO_ALTO = 32;
        private static final int ESPACIO_HORIZONTAL = 18;
        private static final int ESPACIO_VERTICAL = 60;
        private BTree<Producto> arbol;

        public BTreeDrawPanel(BTree<Producto> arbol) {
            this.arbol = arbol;
            setBackground(new Color(255, 255, 180)); // fondo amarillo claro como la imagen
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (arbol == null || arbol.root == null) {
                g.setColor(Color.DARK_GRAY);
                g.setFont(g.getFont().deriveFont(Font.ITALIC, 14f));
                g.drawString("El árbol está vacío", getWidth() / 2 - 60, getHeight() / 2);
                return;
            }
            int centerX = getWidth() / 2;
            dibujarNodo(g, arbol.root, centerX, 30);
        }

        // Calcula el ancho total que ocupa un subárbol para centrarlo
        private int calcularAncho(NodeBTree<Producto> nodo) {
            if (nodo == null) return 0;
            if (nodo.isLeaf || nodo.childs == null || nodo.childs.size() == 0) {
                return Math.max(NODO_ANCHO_MIN, nodo.count * 32 + (nodo.count - 1) * 8);
            }
            int total = 0;
            for (int i = 0; i < nodo.childs.size(); i++) {
                NodeBTree<Producto> hijo = nodo.childs.get(i);
                if (hijo != null) {
                    total += calcularAncho(hijo);
                    if (i < nodo.childs.size() - 1) total += ESPACIO_HORIZONTAL;
                }
            }
            return Math.max(total, Math.max(NODO_ANCHO_MIN, nodo.count * 32 + (nodo.count - 1) * 8));
        }

        // Dibuja el nodo y sus hijos alineados como en la imagen
        private void dibujarNodo(Graphics g, NodeBTree<Producto> nodo, int x, int y) {
            if (nodo == null) return;

            // --- Dibuja el nodo como una caja con claves separadas ---
            int claves = nodo.count;
            int anchoNodo = Math.max(NODO_ANCHO_MIN, claves * 32 + (claves - 1) * 8);
            int altoNodo = NODO_ALTO;

            // Dibuja caja
            g.setColor(new Color(240, 250, 255));
            g.fillRect(x - anchoNodo / 2, y, anchoNodo, altoNodo);
            g.setColor(Color.BLUE.darker());
            g.drawRect(x - anchoNodo / 2, y, anchoNodo, altoNodo);

            // Dibuja divisiones internas para cada clave
            for (int i = 1; i < claves; i++) {
                int xDiv = x - anchoNodo / 2 + i * 32 + (i - 1) * 8;
                g.drawLine(xDiv, y, xDiv, y + altoNodo);
            }

            // Dibuja claves (solo IDs, centrados en cada celda)
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            for (int i = 0; i < claves; i++) {
                Producto key = nodo.keys.get(i);
                String id = key != null ? key.getId() : "";
                int celdaAncho = 32;
                int xCelda = x - anchoNodo / 2 + i * (celdaAncho + 8);
                int textoAncho = fm.stringWidth(id);
                g.drawString(id, xCelda + (celdaAncho - textoAncho) / 2, y + altoNodo / 2 + fm.getAscent() / 2 - 4);
            }

            // --- Dibuja hijos debajo, alineados horizontalmente ---
            int hijos = nodo.childs.size();
            if (hijos == 0) return;

            // Calcula el ancho total ocupado por todos los hijos
            int totalAnchoHijos = 0;
            int[] anchosHijos = new int[hijos];
            for (int i = 0; i < hijos; i++) {
                NodeBTree<Producto> hijo = nodo.childs.get(i);
                int anchoHijo = calcularAncho(hijo);
                anchosHijos[i] = anchoHijo;
                totalAnchoHijos += anchoHijo;
                if (i < hijos - 1) totalAnchoHijos += ESPACIO_HORIZONTAL;
            }

            int xInicio = x - totalAnchoHijos / 2;
            int xHijo = xInicio;
            int yHijo = y + altoNodo + ESPACIO_VERTICAL;

            for (int i = 0; i < hijos; i++) {
                NodeBTree<Producto> hijo = nodo.childs.get(i);
                if (hijo != null) {
                    // Línea desde borde inferior de la caja padre al borde superior de la caja hijo
                    int xPadre = x - anchoNodo / 2 + i * (anchoNodo / hijos) + (anchoNodo / hijos) / 2;
                    int xCentroHijo = xHijo + anchosHijos[i] / 2;
                    g.setColor(Color.BLACK);
                    g.drawLine(xPadre, y + altoNodo, xCentroHijo, yHijo);
                    dibujarNodo(g, hijo, xCentroHijo, yHijo);
                }
                xHijo += anchosHijos[i] + ESPACIO_HORIZONTAL;
            }
        }
    }

    // --- PANEL CENTRAL: ÁRBOL B+ VISUAL ---
    class BTreePanel extends JPanel {
        private static final int NODE_HEIGHT = 40;
        private static final int NODE_MIN_WIDTH = 60;
        private static final int LEVEL_HEIGHT = 70;
        private BTree<Producto> arbol;

        public BTreePanel(BTree<Producto> arbol) {
            this.arbol = arbol;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (arbol == null || arbol.root == null) {
                g.setColor(Color.DARK_GRAY);
                g.setFont(g.getFont().deriveFont(Font.ITALIC, 14f));
                g.drawString("El árbol está vacío", getWidth() / 2 - 60, getHeight() / 2);
                return;
            }
            // Dibuja el árbol verticalmente, hijos debajo del padre
            drawNode(g, arbol.root, getWidth() / 2, 30, getWidth() / 2);
        }

        // Dibuja el nodo y sus hijos debajo, centrados horizontalmente
        private int drawNode(Graphics g, NodeBTree<Producto> node, int x, int y, int anchoTotal) {
            if (node == null) return x;

            // Prepara texto para mostrar las claves del nodo separadas por |
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < node.count; i++) {
                Producto key = node.keys.get(i);
                if (key != null) {
                    sb.append(key.getId());
                    if (i < node.count - 1) sb.append(" | ");
                }
            }
            String texto = sb.toString();

            // Calcula ancho de nodo según texto
            FontMetrics fm = g.getFontMetrics();
            int anchoTexto = fm.stringWidth(texto);
            int anchoNodo = Math.max(NODE_MIN_WIDTH, anchoTexto + 20);

            // Dibuja nodo (color diferente si es hoja)
            g.setColor(node.isLeaf ? new Color(180, 255, 180) : new Color(200, 220, 255));
            g.fillRoundRect(x - anchoNodo / 2, y, anchoNodo, NODE_HEIGHT, 15, 15);
            g.setColor(Color.BLUE.darker());
            g.drawRoundRect(x - anchoNodo / 2, y, anchoNodo, NODE_HEIGHT, 15, 15);
            g.setColor(Color.BLACK);
            g.drawString(texto, x - anchoTexto / 2, y + NODE_HEIGHT - 12);

            // Dibuja flecha next si es hoja
            if (node.isLeaf && node.next != null) {
                int x2 = x + anchoNodo / 2 + 20;
                int y2 = y + NODE_HEIGHT / 2;
                g.setColor(Color.GRAY);
                g.drawLine(x + anchoNodo / 2, y + NODE_HEIGHT / 2, x2, y2);
                g.fillPolygon(
                    new int[]{x2, x2 - 6, x2 - 6},
                    new int[]{y2, y2 - 6, y2 + 6},
                    3
                );
            }

            // Dibuja hijos y líneas recursivamente (vertical)
            int hijos = node.childs.size();
            if (hijos == 0) return x;
            int totalWidth = anchoTotal;
            int childY = y + NODE_HEIGHT + LEVEL_HEIGHT;
            int childWidth = totalWidth / Math.max(hijos, 1);

            int[] childXs = new int[hijos];
            int left = x - totalWidth / 2 + childWidth / 2;
            for (int i = 0; i < hijos; i++) {
                NodeBTree<Producto> hijo = node.childs.get(i);
                int cx = left + i * childWidth;
                if (hijo != null) {
                    childXs[i] = drawNode(g, hijo, cx, childY, childWidth);
                    // Línea vertical hacia el hijo
                    g.setColor(Color.BLACK);
                    g.drawLine(x, y + NODE_HEIGHT, childXs[i], childY);
                }
            }
            return x;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(700, 500);
        }
    }

    // --- Panel para animar la ruta óptima ---
    class AnimacionRutaPanel extends JPanel {
        private GraphLink<String> grafo;
        private estructura.ArrayList<String> ruta;
        private estructura.HashMap<String, Point> posiciones = new estructura.HashMap<>();
        private int pasoActual = -1;

        public AnimacionRutaPanel(GraphLink<String> grafo, estructura.ArrayList<String> ruta) {
            this.grafo = grafo;
            this.ruta = ruta;
            setBackground(Color.WHITE);
            calcularPosicionesCirculares();
        }

        private void calcularPosicionesCirculares() {
            int n = grafo.listVertex.size();
            int cx = 400, cy = 250, radio = 200;
            Node<Vertex<String>> aux = grafo.listVertex.getHead();
            int i = 0;
            while (aux != null) {
                double ang = 2 * Math.PI * i / n;
                int x = (int) (cx + radio * Math.cos(ang));
                int y = (int) (cy + radio * Math.sin(ang));
                posiciones.put(aux.data.getData(), new Point(x, y));
                i++;
                aux = aux.next;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Node<Vertex<String>> aux = grafo.listVertex.getHead();
            while (aux != null) {
                String from = aux.data.getData();
                Point p1 = posiciones.get(from);
                Node<Edge<String>> ady = aux.data.getListAdj().getHead();
                while (ady != null) {
                    String to = ady.data.getDestination().getData();
                    Point p2 = posiciones.get(to);
                    if (p1 != null && p2 != null) {
                        if (esAristaEnRuta(from, to) && indiceEnRuta(from, to) <= pasoActual) {
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setColor(Color.GREEN.darker());
                            g2.setStroke(new BasicStroke(4));
                            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                            g2.setStroke(new BasicStroke(1));
                        } else {
                            g.setColor(Color.LIGHT_GRAY);
                            g.drawLine(p1.x, p1.y, p2.x, p2.y);
                        }
                        int mx = (p1.x + p2.x) / 2;
                        int my = (p1.y + p2.y) / 2;
                        g.setColor(Color.BLUE);
                        g.drawString(String.valueOf(ady.data.getWeight()), mx, my);
                    }
                    ady = ady.next;
                }
                aux = aux.next;
            }
            ArrayList<String> claves = posiciones.keySet();
            for (int i = 0; i < claves.size(); i++) {
                String v = claves.get(i);
                Point p = posiciones.get(v);
                g.setColor(Color.ORANGE);
                g.fillOval(p.x - 25, p.y - 25, 50, 50);
                g.setColor(Color.BLACK);
                g.drawOval(p.x - 25, p.y - 25, 50, 50);
                g.drawString(v, p.x - 18, p.y + 5);
            }
            if (pasoActual >= 0 && pasoActual < ruta.size()) {
                String actual = ruta.get(pasoActual);
                Point p = posiciones.get(actual);
                if (p != null) {
                    g.setColor(Color.RED);
                    g.fillOval(p.x - 30, p.y - 30, 60, 60);
                    g.setColor(Color.BLACK);
                    g.drawOval(p.x - 30, p.y - 30, 60, 60);
                    g.setColor(Color.WHITE);
                    g.drawString(actual, p.x - 18, p.y + 5);
                }
            }
        }

        public void iniciarAnimacion() {
            new Thread(() -> {
                for (int i = 0; i < ruta.size(); i++) {
                    pasoActual = i;
                    repaint();
                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).start();
        }

        private boolean esAristaEnRuta(String from, String to) {
            for (int i = 0; i < ruta.size() - 1; i++) {
                if (ruta.get(i).equals(from) && ruta.get(i + 1).equals(to)) {
                    return true;
                }
            }
            return false;
        }

        private int indiceEnRuta(String from, String to) {
            for (int i = 0; i < ruta.size() - 1; i++) {
                if (ruta.get(i).equals(from) && ruta.get(i + 1).equals(to)) {
                    return i + 1;
                }
            }
            return -1;
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

