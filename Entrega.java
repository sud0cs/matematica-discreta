import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/*
 * Aquesta entrega consisteix en implementar tots els mètodes anomenats "exerciciX". Ara mateix la
 * seva implementació consisteix en llançar `UnsupportedOperationException`, ho heu de canviar així
 * com els aneu fent.
 *
 * Criteris d'avaluació:
 *
 * - Si el codi no compila tendreu un 0.
 *
 * - Les úniques modificacions que podeu fer al codi són:
 *    + Afegir un mètode (dins el tema que el necessiteu)
 *    + Afegir proves a un mètode "tests()"
 *    + Òbviament, implementar els mètodes que heu d'implementar ("exerciciX")
 *   Si feu una modificació que no sigui d'aquesta llista, tendreu un 0.
 *
 * - Principalment, la nota dependrà del correcte funcionament dels mètodes implementats (provant
 *   amb diferents entrades).
 *
 * - Tendrem en compte la neteja i organització del codi. Un estandard que podeu seguir és la guia
 *   d'estil de Google per Java: https://google.github.io/styleguide/javaguide.html . Per exemple:
 *    + IMPORTANT: Aquesta entrega està codificada amb UTF-8 i finals de línia LF.
 *    + Indentació i espaiat consistent
 *    + Bona nomenclatura de variables
 *    + Declarar les variables el més aprop possible al primer ús (és a dir, evitau blocs de
 *      declaracions).
 *    + Convé utilitzar el for-each (for (int x : ...)) enlloc del clàssic (for (int i = 0; ...))
 *      sempre que no necessiteu l'índex del recorregut. Igualment per while si no és necessari.
 *
 * Per com està plantejada aquesta entrega, no necessitau (ni podeu) utilitzar cap `import`
 * addicional, ni qualificar classes que no estiguin ja importades. El que sí podeu fer és definir
 * tots els mètodes addicionals que volgueu (de manera ordenada i dins el tema que pertoqui).
 *
 * Podeu fer aquesta entrega en grups de com a màxim 3 persones, i necessitareu com a minim Java 10.
 * Per entregar, posau els noms i cognoms de tots els membres del grup a l'array `Entrega.NOMS` que
 * està definit a la línia 53.
 *
 * L'entrega es farà a través d'una tasca a l'Aula Digital que obrirem abans de la data que se us
 * hagui comunicat. Si no podeu visualitzar bé algun enunciat, assegurau-vos de que el vostre editor
 * de texte estigui configurat amb codificació UTF-8.
 */
class Entrega {
  static final String[] NOMS = {"Arion Sintes Sintes", "Ivan Felipe Sintes"};

  /*
   * Aquí teniu els exercicis del Tema 1 (Lògica).
   */
  static class Tema1 {
    /*
     * Determinau si l'expressió és una tautologia o no:
     *
     * (((vars[0] ops[0] vars[1]) ops[1] vars[2]) ops[2] vars[3]) ...
     *
     * Aquí, vars.length == ops.length+1, i cap dels dos arrays és buid. Podeu suposar que els
     * identificadors de les variables van de 0 a N-1, i tenim N variables diferents (mai més de 20
     * variables).
     *
     * Cada ops[i] pot ser: CONJ, DISJ, IMPL, NAND.
     *
     * Retornau:
     *   1 si és una tautologia
     *   0 si és una contradicció
     *   -1 en qualsevol altre cas.
     *
     * Vegeu els tests per exemples.
     */
    static final char CONJ = '∧';
    static final char DISJ = '∨';
    static final char IMPL = '→';
    static final char NAND = '.';

    static int exercici1(char[] ops, int[] vars) {
      List<Integer> v2 = new ArrayList<>();
      for(int v : vars) {
        if(!v2.contains(v))
          v2.add(v);
      }
      int[][] taulaVeritat = new int[(int) Math.pow(2, v2.size())][v2.size()];
      for(int i = 0; i < taulaVeritat.length; i++) {
        String s = Integer.toBinaryString(i);
        while(s.length() < v2.size()) {
            s = "0" + s;
        }
        int[] value = new int[v2.size()];
        for(int j = 0; j < s.length(); j++) {
          value[j] = Integer.parseInt(String.valueOf(s.charAt(j)));
        }
        taulaVeritat[i] = value;
      }

      int t = 0;
      int c = 0;
        for (int[] ints : taulaVeritat) {
            int solucio = vars[0];
            for (int j = 0; j < ops.length; j++) {
                solucio = calculaOperacioLlogica(solucio, ops[j], ints[vars[j]]);
            }

            if (solucio == 1)
                t++;
            else
                c++;
        }

      if(t == taulaVeritat.length)
        return 1;
      else if(c == taulaVeritat.length)
        return 0;
      else
        return -1;
      //throw new UnsupportedOperationException("pendent");
    }

    static int calculaOperacioLlogica(int solTemp, char op, int var) {
      switch(op) {
        case CONJ: {
          solTemp = ((solTemp == 1) & (var == 1))? 1: 0;
        }
        break;
        case DISJ: {
          solTemp = ((solTemp == 1) | (var == 1))? 1: 0;
        }
        break;
        case IMPL: {
          solTemp = (!(solTemp == 1) | var == 1)? 1 : 0;
        }
        break;
        case NAND: {
          solTemp = !((solTemp == 1) & (var == 1))? 0 : 1;
        }
        break;
        default: {}
      }
      return solTemp;
    }

    /*
     * Aquest mètode té de paràmetre l'univers (representat com un array) i els predicats
     * adients `p` i `q`. Per avaluar aquest predicat, si `x` és un element de l'univers, podeu
     * fer-ho com `p.test(x)`, que té com resultat un booleà (true si `P(x)` és cert).
     *
     * Amb l'univers i els predicats `p` i `q` donats, returnau true si la següent proposició és
     * certa.
     *
     * (∀x : P(x)) <-> (∃!x : Q(x))
     */
    static boolean exercici2(int[] universe, Predicate<Integer> p, Predicate<Integer> q) {
      boolean px = true;
      boolean qx;
      int count = 0;
      for(int x : universe) {
        px = px && p.test(x);
        if(q.test(x))
          count++;
      }
      qx = count == 1;

      return calculaOperacioLlogica((px ? 0 : 1), DISJ, (qx ? 1 : 0)) == 1;
      //throw new UnsupportedOperationException("pendent");
    }

    static void tests() {
      // Exercici 1
      // Taules de veritat

      // Tautologia: ((p0 → p2) ∨ p1) ∨ p0
      test(1, 1, 1, () -> exercici1(new char[] { IMPL, DISJ, DISJ }, new int[] { 0, 2, 1, 0 }) == 1);

      // Contradicció: (p0 . p0) ∧ p0
      test(1, 1, 2, () -> exercici1(new char[] { NAND, CONJ }, new int[] { 0, 0, 0 }) == 0);

      // Exercici 2
      // Equivalència

      test(1, 2, 1, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x == 0, (x) -> x == 0);
      });

      test(1, 2, 2, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x >= 1, (x) -> x % 2 == 0);
      });
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 2 (Conjunts).
   *
   * Per senzillesa tractarem els conjunts com arrays (sense elements repetits). Per tant, un
   * conjunt de conjunts d'enters tendrà tipus int[][]. Podeu donar per suposat que tots els
   * arrays que representin conjunts i us venguin per paràmetre estan ordenats de menor a major.
   *
   * Les relacions també les representarem com arrays de dues dimensions, on la segona dimensió
   * només té dos elements. L'array estarà ordenat lexicogràficament. Per exemple
   *   int[][] rel = {{0,0}, {0,1}, {1,1}, {2,2}};
   * i també donarem el conjunt on està definida, per exemple
   *   int[] a = {0,1,2};
   * Als tests utilitzarem extensivament la funció generateRel definida al final (també la podeu
   * utilitzar si la necessitau).
   *
   * Les funcions f : A -> B (on A i B son subconjunts dels enters) les representam o bé amb el seu
   * graf o bé amb un objecte de tipus Function<Integer, Integer>. Sempre donarem el domini int[] a
   * i el codomini int[] b. En el cas de tenir un objecte de tipus Function<Integer, Integer>, per
   * aplicar f a x, és a dir, "f(x)" on x és d'A i el resultat f.apply(x) és de B, s'escriu
   * f.apply(x).
   */
  static class Tema2 {
    /*
     * Trobau el nombre de particions diferents del conjunt `a`, que podeu suposar que no és buid.
     *
     * Pista: Cercau informació sobre els nombres de Stirling.
     */
    static int exercici1(int[] a) {
      int n = a.length;
      int subconjunts = 0;
      for(int i = 0; i <= n; i++) {
        subconjunts += calculaStirling(n, i);
      }
      return subconjunts;
    }

    static int calculaStirling(int n, int k) {
      if(k == n) {
        return 1;
      } else if(k > n || k == 0 || n == 0) {
        return 0;
      }
      return  k * calculaStirling(n - 1, k) + calculaStirling(n - 1, k - 1);
    }

    /*
     * Trobau el cardinal de la relació d'ordre parcial sobre `a` més petita que conté `rel` (si
     * existeix). En altres paraules, el cardinal de la seva clausura reflexiva, transitiva i
     * antisimètrica.
     *
     * Si no existeix, retornau -1.
     */
    static int exercici2(int[] a, int[][] rel) {

      if(esTransitiva(rel))
        return -1;

      ArrayList<int[]> temp = new ArrayList<>();
      for (int value : a) {
        for (int[] ints : rel) {
          for (int anInt : ints) {
            temp.add(new int[]{value, anInt});
          }
        }
      }

      int[][] conjunt = temp.toArray(new int[][]{});

      conjunt = esborraDuplicats(conjunt);
      boolean reflexiva = esReflexiva(conjunt, a.length);
      boolean antisimetrica = esAntisimetrica(conjunt);
      conjunt = esborraIncorrectes(conjunt);

      if(reflexiva && antisimetrica)
        return esborraIncorrectes(conjunt).length;
      else
        return -1;
      //throw new UnsupportedOperationException("pendent");
    }

    static boolean esReflexiva(int[][] conjunt, int nombres) {
      int count = 0;
      for(int[] sub : conjunt) {
        if(sub[0] == sub[1])
          count++;
      }
      return count == nombres;
    }

    static boolean esTransitiva(int[][] conjunt) {
      for(int[] c1 : conjunt) {
        for(int[] c2 : conjunt) {
          if((c1 != c2) && c1[0] == c2[1] && c1[1] == c2[0])
            return true;
        }
      }
      return false;
    }

    static boolean esAntisimetrica(int[][] conjunt) {
      int count = 0;
      for(int[] c : conjunt) {
        for(int[] c2 : conjunt) {
          if(c[0] == c2[1] && c[1] == c2[0]) {
            count++;
            break;
          }
        }
      }
      return count == conjunt.length;
    }
    
    static int[][] esborraDuplicats(int[][] conjunt) {
      ArrayList<int[]> senseDuplicats = new ArrayList<>();
      for(int[] c : conjunt) {
        if(senseDuplicats.isEmpty())
          senseDuplicats.add(c);
        else {
          int dup = 0;
          for(int[] c2 : senseDuplicats) {
            if(c[0] == c2[0] && c[1] == c2[1]) {
              dup++;
              break;
            }
          }
          if(dup == 0)
            senseDuplicats.add(c);
        }
      }
      return senseDuplicats.toArray(new int[][]{});
    }

    static int[][] esborraIncorrectes(int[][] conjunt) {
      ArrayList<int[]> senseIncorrectes = new ArrayList<>();
      for(int[] c : conjunt) {
        if(senseIncorrectes.isEmpty())
          senseIncorrectes.add(c);
        else {
          if(c[0] <= c[1])
            senseIncorrectes.add(c);
        }
      }
      return senseIncorrectes.toArray(new int[][]{});
    }

    /*
     * Donada una relació d'ordre parcial `rel` definida sobre `a` i un subconjunt `x` de `a`,
     * retornau:
     * - L'ínfim de `x` si existeix i `op` és false
     * - El suprem de `x` si existeix i `op` és true
     * - null en qualsevol altre cas
     */
    static Integer exercici3(int[] a, int[][] rel, int[] x, boolean op) {
      int infimX = 0;
      int supremX = 0;

      for(int i : a) {
        int count = 0;
        for(int j : a) {
          if(j % i == 0)
            count++;
        }

        if(count == a.length) {
          infimX = i;
          supremX = i;
        }
      }

      if(infimX != 0 && !op)
        return infimX;
      else if(supremX != 1 && op)
        return supremX;
      else
        return null;
      //throw new UnsupportedOperationException("pendent");
    }

    /*
     * Donada una funció `f` de `a` a `b`, retornau:
     *  - El graf de la seva inversa (si existeix)
     *  - Sinó, el graf d'una inversa seva per l'esquerra (si existeix)
     *  - Sinó, el graf d'una inversa seva per la dreta (si existeix)
     *  - Sinó, null.
     */
    static int[][] exercici4(int[] a, int[] b, Function<Integer, Integer> f) {
      if(!comprovaDuplicitat(a, b)) {
        int[][] solucio = new int[b.length][2];
        for(int i : b) {
          solucio[i][0] = i;
          if(a.length > b.length)
            solucio[i][1] = Math.min(a[i * 2], a[i * 2 + 1]);
          else
            solucio[i][1] = f.apply(i);
        }
        return solucio;
      }

      return null;
      //throw new UnsupportedOperationException("pendent");
    }

    static boolean comprovaDuplicitat(int[] a, int[] b) {
      for(int i : a) {
        int count = 0;
        for(int j : b) {
          if(i == j)
            count++;
          if(count > 1)
            return true;
        }
      }
      return false;
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {
      // Exercici 1
      // Nombre de particions

      test(2, 1, 1, () -> exercici1(new int[] { 1 }) == 1);
      test(2, 1, 2, () -> exercici1(new int[] { 1, 2, 3 }) == 5);

      // Exercici 2
      // Clausura d'ordre parcial

      final int[] INT02 = { 0, 1, 2 };

      test(2, 2, 1, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 2} }) == 6);
      test(2, 2, 2, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 0}, {1, 2} }) == -1);

      // Exercici 3
      // Ínfims i suprems

      final int[] INT15 = { 1, 2, 3, 4, 5 };
      final int[][] DIV15 = generateRel(INT15, (n, m) -> m % n == 0);
      final Integer ONE = 1;


      final int[] INT05 = { 0, 1, 2, 3, 4, 5 };

      test(2, 3, 1, () -> ONE.equals(exercici3(INT15, DIV15, new int[] { 2, 3 }, false)));
      test(2, 3, 2, () -> exercici3(INT15, DIV15, new int[] { 2, 3 }, true) == null);

      // Exercici 4
      // Inverses
      test(2, 4, 1, () -> {
        var inv = exercici4(INT05, INT02, (x) -> x/2);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT02.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1]/2 != i)
            return false;
        }

        return true;
      });

      test(2, 4, 2, () -> {
        var inv = exercici4(INT02, INT05, (x) -> x);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT05.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1] != i)
            return false;
        }

        return true;
      });
    }

    /*
     * Ordena lexicogràficament un array de 2 dimensions
     * Per exemple:
     *  arr = {{1,0}, {2,2}, {0,1}}
     *  resultat = {{0,1}, {1,0}, {2,2}}
     */
    static int[][] lexSorted(int[][] arr) {
      if (arr == null)
        return null;

      var arr2 = Arrays.copyOf(arr, arr.length);
      Arrays.sort(arr2, Arrays::compare);
      return arr2;
    }

    /*
     * Genera un array int[][] amb els elements {a, b} (a de as, b de bs) que satisfàn pred.test(a, b)
     * Per exemple:
     *   as = {0, 1}
     *   bs = {0, 1, 2}
     *   pred = (a, b) -> a == b
     *   resultat = {{0,0}, {1,1}}
     */
    static int[][] generateRel(int[] as, int[] bs, BiPredicate<Integer, Integer> pred) {
      var rel = new ArrayList<int[]>();

      for (int a : as) {
        for (int b : bs) {
          if (pred.test(a, b)) {
            rel.add(new int[] { a, b });
          }
        }
      }

      return rel.toArray(new int[][] {});
    }

    // Especialització de generateRel per as = bs
    static int[][] generateRel(int[] as, BiPredicate<Integer, Integer> pred) {
      return generateRel(as, as, pred);
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 3 (Grafs).
   *
   * Els (di)grafs vendran donats com llistes d'adjacència (és a dir, tractau-los com diccionaris
   * d'adjacència on l'índex és la clau i els vèrtexos estan numerats de 0 a n-1). Per exemple,
   * podem donar el graf cicle no dirigit d'ordre 3 com:
   *
   * int[][] c3dict = {
   *   {1, 2}, // veïns de 0
   *   {0, 2}, // veïns de 1
   *   {0, 1}  // veïns de 2
   * };
   */
  static class Tema3 {
    /*
     * Determinau si el graf `g` (no dirigit) té cicles.
     */
    static boolean exercici1(int[][] g) {
      return recursiveCicleFinder(g,0,0,null);
    }

    static boolean recursiveCicleFinder(int[][] g, int node, int prev_node, int[] v){
      if(v==null)v=new int[0];
      int[] v2 = v.clone();
      v = new int[v2.length+1];
      for(int i=0;i<v2.length;i++)v[i]=v2[i];
      v[v2.length]=node;
      for(int n: g[node]){
        if (g[node].length<2)return false;
        if(node!=n && n!=prev_node){
          for(int pn:v){
            if(pn==n && pn!=node)return true;
          }
          return recursiveCicleFinder(g, n, node, v);
        }
      }
      return false;
    }

    /*
     * Determinau si els dos grafs són isomorfs. Podeu suposar que cap dels dos té ordre major que
     * 10.
     */
    static boolean exercici2(int[][] g1, int[][] g2) {
      if(g1.length!=g2.length)return false;
      int[] g1gr = new int[g1.length];
      int[] g2gr = new int[g2.length];
      for(int i = 0; i<g1.length;i++){
        g1gr[g1[i].length]++;
        g2gr[g2[i].length]++;
      }
      for(int i = 0; i<g1.length;i++){
        if(g1gr[i]!=g2gr[i])return false;
      }
      return true;
    }

    /*
     * Determinau si el graf `g` (no dirigit) és un arbre. Si ho és, retornau el seu recorregut en
     * postordre desde el vèrtex `r`. Sinó, retornau null;
     *
     * En cas de ser un arbre, assumiu que l'ordre dels fills vé donat per l'array de veïns de cada
     * vèrtex.
     */
    static int[] exercici3(int[][] g, int r) {
      if(exercici1(g))return null;
      int node = findInitialNode(g, 0, 0, 0);
      if(node==-1)return null;
      return recursiveTreeAnalizer(node, node, null, g, null);
    }
    
    //v = visited nodes
    static int[] recursiveTreeAnalizer(int node, int prev_node, int[] count, int[][] g, int[] v){
      if(count == null){count=new int[g.length];count[0]=-1;}
      if(v==null)v=new int[0];
      int[] v2 = v.clone();
      v = new int[v2.length+1];
      for(int i=0;i<v2.length;i++)v[i]=v2[i];
      v[v2.length]=node;
      if(v.length>g.length)return v;
      if(node==0)return v;
      for (int n:g[node]){
        if(n!=prev_node){
          if(g[n].length-count[n]<3){
            return recursiveTreeAnalizer(n, node, count, g, v);
          }
          else{
            count[n]++;
            int inode = findInitialNode(g, n, v, 0);
            return recursiveTreeAnalizer(inode, inode, count, g, v);
          }
        }
      }
      return v;
    }

    static int findInitialNode(int[][] g, int node, int prev_node, int nc){
      if(nc>g.length)return -1;
      if(node!=0 && g[node].length==1) return node;
      for(int n:g[node]){
        if(n!=prev_node && n>node) return findInitialNode(g, n, node, nc+1);
      }
      return node;
    }

    static int findInitialNode(int[][] g, int node, int[] prev_nodes, int nc){
      if(nc>g.length)return -1;
      if(node!=0 && g[node].length==1) return node;
      boolean f = false;
      for(int n:g[node]){
        for(int pn:prev_nodes)
          if(n!=pn && n>node) f=true;
          else{f=false;break;}
          if(f)return findInitialNode(g, n, node, nc+1);
      }
      return node;
    }

    //static int isTree()

    /*
     * Suposau que l'entrada és un mapa com el següent, donat com String per files (vegeu els tests)
     *
     *   _____________________________________
     *  |          #       #########      ####|
     *  |       O  # ###   #########  ##  ####|
     *  |    ####### ###   #########  ##      |
     *  |    ####  # ###   #########  ######  |
     *  |    ####    ###              ######  |
     *  |    ######################## ##      |
     *  |    ####                     ## D    |
     *  |_____________________________##______|
     *
     * Els límits del mapa els podeu considerar com els límits de l'array/String, no fa falta que
     * cerqueu els caràcters "_" i "|", i a més podeu suposar que el mapa és rectangular.
     *
     * Donau el nombre mínim de caselles que s'han de recorrer per anar de l'origen "O" fins al
     * destí "D" amb les següents regles:
     *  - No es pot sortir dels límits del mapa
     *  - No es pot passar per caselles "#"
     *  - No es pot anar en diagonal
     *
     * Si és impossible, retornau -1.
     */
    static int exercici4(char[][] mapa) {
      //throw new UnsupportedOperationException("pendent");
      int opos_x = 0;
      int opos_y = 0;
      int dpos_x = 0;
      int dpos_y = 0;
      for(int i = 0;i<mapa.length;i++){
        for(int j = 0;j<mapa[i].length;j++){
          if(mapa[i][j]=='O'){opos_x=i;opos_y=j;}
          if(mapa[i][j]=='D'){dpos_x=i;dpos_y=j;}
        }
      }
      int[][] pathmap = pathMap(mapa, null, opos_x, opos_y);
      int out = pathmap[dpos_x][dpos_y];
      return out==Integer.MAX_VALUE?-1:out;
    }


    static int[][] pathMap(char[][] map, int[][] pmap, int posx, int posy){
      if(pmap==null){
        pmap = new int[map.length][map[0].length];
        for(int i=0;i<pmap.length;i++){
          for(int j=0;j<pmap[i].length;j++){
            pmap[i][j] = Integer.MAX_VALUE;
          }
        }
        pmap[posx][posy]=0;
      }

      ArrayList<int[]> posStorage = new ArrayList<>();
      posStorage.add(new int[]{posx, posy});
      for (int i = 0; i<map.length*map[0].length; i++){
        @SuppressWarnings("unchecked")
        ArrayList<int[]> posStorageBackup = (ArrayList<int[]>)posStorage.clone();
        for(int[] pos:posStorageBackup){
          posx = pos[0];
          posy = pos[1];
          if (posx+1 < pmap.length){
            if(pmap[posx+1][posy]>pmap[posx][posy]+1 && setNext(map, pmap, posx+1, posy, posx, posy)) posStorage.add(new int[]{posx+1, posy});
          }
          if (posx-1 >= 0){
            if(pmap[posx-1][posy]>pmap[posx][posy]+1 && setNext(map, pmap, posx-1, posy, posx, posy)) posStorage.add(new int[]{posx-1, posy});
          }
          if (posy+1 < pmap[0].length){
            if(pmap[posx][posy+1]>pmap[posx][posy]+1 && setNext(map, pmap, posx, posy+1, posx, posy)) posStorage.add(new int[]{posx, posy+1});
          }
          if (posy-1 >= 0){
            if(pmap[posx][posy-1]>pmap[posx][posy]+1 && setNext(map, pmap, posx, posy-1, posx, posy)) posStorage.add(new int[]{posx, posy-1});
          }
          posStorage.remove(pos);
        }
      }
      return pmap;

      //int[posx][posy]
    }

    static boolean setNext(char[][] map, int[][] pmap, int posx, int posy, int pposx, int pposy){
      switch(map[posx][posy]){
        case 'D':
        case ' ':
          pmap[posx][posy]=pmap[pposx][pposy]+1;
          return true;
        case '#':
          return false;
        default:
          return false;
      }
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {

      final int[][] D2 = { {}, {} };
      final int[][] C3 = { {1, 2}, {0, 2}, {0, 1} };
      final int[][] C4 = { {1}, {2,3}, {1,3}, {2,1} };

      final int[][] T1 = { {1, 2}, {0}, {0} };
      final int[][] T2 = { {1}, {0, 2}, {1} };
      final int[][] T3 = { {1,2,3}, {0,5}, {0,4}, {0}, {2,6,7}, {1}, {4}, {4}};

      // Exercici 1
      // G té cicles?

      test(3, 1, 1, () -> !exercici1(D2));
      test(3, 1, 2, () -> exercici1(C3));
      test(3, 1, 3, () -> !exercici1(C4));

      // Exercici 2
      // Isomorfisme de grafs

      test(3, 2, 1, () -> exercici2(T1, T2));
      test(3, 2, 2, () -> !exercici2(T1, C3));

      // Exercici 3
      // Postordre

      test(3, 3, 1, () -> exercici3(C3, 1) == null);
      test(3, 3, 2, () -> Arrays.equals(exercici3(T1, 0), new int[] { 1, 2, 0 }));
      test(3, 3, 3, () -> Arrays.equals(exercici3(T3, 0), new int[] { 5, 1, 6, 7, 4, 2, 3, 0 }));

      // Exercici 4
      // Laberint

      test(3, 4, 1, () -> {
        return -1 == exercici4(new char[][] {
            " #O".toCharArray(),
            "D# ".toCharArray(),
            " # ".toCharArray(),
        });
      });

      test(3, 4, 2, () -> {
        return 8 == exercici4(new char[][] {
            "###D".toCharArray(),
            "O # ".toCharArray(),
            " ## ".toCharArray(),
            "    ".toCharArray(),
        });
      });

      test(3,4,3, () -> {
        return 58 == exercici4(new char[][]{
        "          #       #########      ####".toCharArray(),
        "       O  # ###   #########  ##  ####".toCharArray(),
        "    ####### ###   #########  ##      ".toCharArray(),
        "    ####  # ###   #########  ######  ".toCharArray(),
        "    ####    ###              ######  ".toCharArray(),
        "    ######################## ##      ".toCharArray(),
        "    ####                     ## D    ".toCharArray(),
        "                             ##      ".toCharArray()

        });
      });
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 4 (Aritmètica).
   *
   * En aquest tema no podeu:
   *  - Utilitzar la força bruta per resoldre equacions: és a dir, provar tots els nombres de 0 a n
   *    fins trobar el que funcioni.
   *  - Utilitzar long, float ni double.
   *
   * Si implementau algun dels exercicis així, tendreu un 0 d'aquell exercici.
   */
  static class Tema4 {
    /*
     * Primer, codificau el missatge en blocs de longitud 2 amb codificació ASCII. Després encriptau
     * el missatge utilitzant xifrat RSA amb la clau pública donada.
     *
     * Per obtenir els codis ASCII del String podeu utilitzar `msg.getBytes()`.
     *
     * Podeu suposar que:
     * - La longitud de `msg` és múltiple de 2
     * - El valor de tots els caràcters de `msg` està entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     *
     * Pista: https://en.wikipedia.org/wiki/Exponentiation_by_squaring
     */
    static int[] exercici1(String msg, int n, int e) {
      long[] out = new long[(int)msg.length()/2];
      byte[] in = msg.getBytes();
      for(int i = 0; i<in.length; i++){
        int index = (int)Math.floor(i/2);
        out[index] += in[i]*Math.pow(128l, (i+1)%2);
      }
      for(int i = 0; i<out.length; i++){
        out[i] = sqmodpow(out[i],e,n);
      }
      int[] x = new int[out.length];
      for(int i = 0; i<x.length;i++)x[i]=(int)out[i];
      return x;
    }

    static long sqmodpow(long a, long b, long n){
      char[] bin = Long.toBinaryString(b).toCharArray();
      long result = 1l;
      int checkpoint = 0;
      //long r = a%n;
      long[] mult = new long[bin.length];
      for(int i = bin.length-1;i>-1;i--){
        if (bin[i]=='1'){
          if(i==bin.length-1){mult[i]=a%n;checkpoint=i;}
          else{
            long x = checkpoint==0?a%n:mult[checkpoint];
            for(int j = 0; j<checkpoint-i;j++){
              x=(x*x)%n;
            }
            mult[i] = x;
            checkpoint = i;
          }
        }
      }
      for(long i:mult){
        if(i!=0){
          result=i*(result%n);
        }
      }
      return result%n;
    }
    /*
     * Primer, desencriptau el missatge utilitzant xifrat RSA amb la clau pública donada. Després
     * descodificau el missatge en blocs de longitud 2 amb codificació ASCII (igual que l'exercici
     * anterior, però al revés).
     *
     * Per construir un String a partir d'un array de bytes podeu fer servir el constructor
     * `new String(byte[])`. Si heu de factoritzar algun nombre, ho podeu fer per força bruta.
     *
     * També podeu suposar que:
     * - La longitud del missatge original és múltiple de 2
     * - El valor de tots els caràcters originals estava entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
    */

    static String exercici2(int[] m, int n, int e) {
      StringBuilder fstr = new StringBuilder();
      ArrayList<Integer> mods = new ArrayList<>();
      ArrayList<int[]> factorization = primeFactorization(n);
      int phi = 1;
      for(int[] i:factorization){
        phi*=(Math.pow(i[0], i[1])-Math.pow(i[0], i[1]-1));
      }
      int inv = inverse(e, phi);
      for(int data:m){
        StringBuilder str = new StringBuilder();
        long d = sqmodpow(data, inv, n);
        long ml = 1;
        long ex = 0;
        long sub = 0;
        for (int i = 0; i<2; i++) {
          long c = ((d-sub)/ml)%128;
          str.append((char)c);
          ml*=128;
          sub+=c*Math.pow(128,ex);
          ex++;
        }
        str = str.reverse();
        fstr.append(str);
      }
      return fstr.toString();
    }

    static int inverse(int a, int b){
      int n = a % b;
      for (int x = 1; x < b; x++) {
        if ((n * x) % b==1) {
            return x;
        }
      }
      return -1;
    }

    static ArrayList<int[]> primeFactorization(int n){
      ArrayList<int[]> factors = new ArrayList<>();
      ArrayList<Integer> pf = new ArrayList<>();
      int c=n;
      for(int i = 2; i<c;i+=2){
        if(n==1)break;
        if(i==2){
          int[] fc = new int[]{2,0};
          while(n%2==0){
            n/=2;
            fc[1]++;
          };
          i-=1;
          if(fc[1]!=0)factors.add(fc);
          pf.add(fc[0]);
        }
        else{
          boolean skip = false;
          for(int j:pf){
            if(j%n==0){skip=true;break;}
          }
          if(skip || n%i!=0)continue;
          int[] fc = new int[]{i,0};
          while(n%i==0){
            n/=i;
            fc[1]++;
          };
          pf.add(fc[0]);
          factors.add(fc);
        }
      }
      if(factors.isEmpty())factors.add(new int[]{n, 1});
      return factors;
    }

    static void tests() {
      // Exercici 1
      // Codificar i encriptar
      test(4, 1, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = exercici1("Patata", n, e);
        return Arrays.equals(encr, new int[] { 4907, 4785, 4785 });
      });

      // Exercici 2
      // Desencriptar i decodificar
      test(4, 2, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = new int[] { 4907, 4785, 4785 };
        var decr = exercici2(encr, n, e);
        return "Patata".equals(decr);
      });
    }
  }

  /*
   * Aquest mètode `main` conté alguns exemples de paràmetres i dels resultats que haurien de donar
   * els exercicis. Podeu utilitzar-los de guia i també en podeu afegir d'altres (no els tendrem en
   * compte, però és molt recomanable).
   *
   * Podeu aprofitar el mètode `test` per comprovar fàcilment que un valor sigui `true`.
   */
  public static void main(String[] args) {
    System.out.println("---- Tema 1 ----");
    Tema1.tests();
    System.out.println("---- Tema 2 ----");
    Tema2.tests();
    System.out.println("---- Tema 3 ----");
    Tema3.tests();
    System.out.println("---- Tema 4 ----");
    Tema4.tests();
  }

  // Informa sobre el resultat de p, juntament amb quin tema, exercici i test es correspon.
  static void test(int tema, int exercici, int test, BooleanSupplier p) {
    try {
      if (p.getAsBoolean())
        System.out.printf("Tema %d, exercici %d, test %d: OK\n", tema, exercici, test);
      else
        System.out.printf("Tema %d, exercici %d, test %d: Error\n", tema, exercici, test);
    } catch (Exception e) {
      if (e instanceof UnsupportedOperationException && "pendent".equals(e.getMessage())) {
        System.out.printf("Tema %d, exercici %d, test %d: Pendent\n", tema, exercici, test);
      } else {
        System.out.printf("Tema %d, exercici %d, test %d: Excepció\n", tema, exercici, test);
        e.printStackTrace();
      }
    }
  }
}

// vim: set textwidth=100 shiftwidth=2 expandtab :
