package model.cua;

import model.NodeHuffman;
import java.util.*;

public class CuaFibonacciHeap implements CuaPrioritat {

    private FibonacciNode minim;
    private int nombreNodes;

    public CuaFibonacciHeap() {
        minim = null;
        nombreNodes = 0;
    }

    @Override
    public void afegir(NodeHuffman node) {
        FibonacciNode nou = new FibonacciNode(node);
        if (minim != null) {
            afegirALlistaArrel(nou);
            if (node.getFrequencia() < minim.getFrequencia()) {
                minim = nou;
            }
        } else {
            minim = nou;
        }
        nombreNodes++;
    }

    @Override
    public NodeHuffman extreure() {
        if (minim == null) return null;

        FibonacciNode minActual = minim;

        // Afegim els fills del mínim a la llista d’arrels
        if (minActual.getFill() != null) {
            FibonacciNode fill = minActual.getFill();
            List<FibonacciNode> fills = new ArrayList<>();
            FibonacciNode actual = fill;
            do {
                fills.add(actual);
                actual = actual.getDreta();
            } while (actual != fill);

            for (FibonacciNode f : fills) {
                f.setPare(null);
                afegirALlistaArrel(f);
            }
        }

        eliminarDeLlistaArrel(minActual);

        if (minActual == minActual.getDreta()) {
            minim = null;
        } else {
            minim = minActual.getDreta();
            consolidar();
        }

        nombreNodes--;
        return minActual.getValor();
    }

    private void afegirALlistaArrel(FibonacciNode node) {
        if (minim == null) {
            node.setDreta(node);
            node.setEsquerra(node);
            minim = node;
        } else {
            node.setEsquerra(minim);
            node.setDreta(minim.getDreta());
            minim.getDreta().setEsquerra(node);
            minim.setDreta(node);
        }
    }

    private void eliminarDeLlistaArrel(FibonacciNode node) {
        node.getEsquerra().setDreta(node.getDreta());
        node.getDreta().setEsquerra(node.getEsquerra());
    }

    private void consolidar() {
        int midaArray = ((int) Math.floor(Math.log(nombreNodes) / Math.log(2))) + 1;
        List<FibonacciNode> aux = new ArrayList<>(Collections.nCopies(midaArray, null));

        List<FibonacciNode> llistaArrel = obtenirLlistaArrel();

        for (FibonacciNode node : llistaArrel) {
            int grau = node.getGrau();
            while (grau >= aux.size()) {
                aux.add(null);
            }

            while (aux.get(grau) != null) {
                FibonacciNode altre = aux.get(grau);
                if (node.getFrequencia() > altre.getFrequencia()) {
                    FibonacciNode temp = node;
                    node = altre;
                    altre = temp;
                }
                enllaçar(altre, node);
                aux.set(grau, null);
                grau++;
                while (grau >= aux.size()) {
                    aux.add(null);
                }
            }
            aux.set(grau, node);
        }

        minim = null;
        for (FibonacciNode node : aux) {
            if (node != null) {
                if (minim == null) {
                    node.setDreta(node);
                    node.setEsquerra(node);
                    minim = node;
                } else {
                    afegirALlistaArrel(node);
                    if (node.getFrequencia() < minim.getFrequencia()) {
                        minim = node;
                    }
                }
            }
        }
    }

    private List<FibonacciNode> obtenirLlistaArrel() {
        List<FibonacciNode> llista = new ArrayList<>();
        if (minim != null) {
            FibonacciNode actual = minim;
            do {
                llista.add(actual);
                actual = actual.getDreta();
            } while (actual != minim);
        }
        return llista;
    }

    private void enllaçar(FibonacciNode y, FibonacciNode x) {
        eliminarDeLlistaArrel(y);
        y.setPare(x);

        if (x.getFill() == null) {
            y.setDreta(y);
            y.setEsquerra(y);
            x.setFill(y);
        } else {
            y.setDreta(x.getFill());
            y.setEsquerra(x.getFill().getEsquerra());
            x.getFill().getEsquerra().setDreta(y);
            x.getFill().setEsquerra(y);
        }

        x.setGrau(x.getGrau() + 1);
        y.setMarca(false);
    }

    @Override
    public int mida() {
        return nombreNodes;
    }

    @Override
    public boolean esBuida() {
        return nombreNodes == 0;
    }
}
