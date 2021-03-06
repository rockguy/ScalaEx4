package patmat

import common._

/**
 * Assignment 4: Huffman coding
 *
 */
object Huffman {

  /**
   * Код Хафмана представлен бинарным деревом.
   *
   * Каждый `Leaf` узел дерева представляет символ алфавита, которое дерево может закодировать.
   * Вес `Leaf` -частотность символа в тексте.
   *
   * Ветви дерева хафмана, `Fork` узлы, представляют множество, содержащее все символы , присутствующие в дереве ниже него.
   * Вес узла `Fork` равен сумме весом его листьев
   */
  abstract class CodeTree
  case class Fork(left: CodeTree, right: CodeTree, chars: List[Char], weight: Int) extends CodeTree
  case class Leaf(char: Char, weight: Int) extends CodeTree


  // Часть 1: Базовая
  def weight(tree: CodeTree): Int = tree match {
    case Fork(left, right, chars, weight) => weight
    case Leaf(char, weight) => weight
  }

  def chars(tree: CodeTree): List[Char] = tree match {
    case Fork(left, right, chars, weight) => chars
    case Leaf(char, weight) => List[Char](char)
  }
  
  def makeCodeTree(left: CodeTree, right: CodeTree) =
    Fork(left, right, chars(left) ::: chars(right), weight(left) + weight(right))



  // Часть 2: Генерация дерева Хафмана

  /**
   * В данном задании вы работаете со списком символов. Эта функция позволяет
   * легко создавать список символов по переданной строке.
   */
  def string2Chars(str: String): List[Char] = str.toList

  /**
   * Эта функция вычисялет для каждого уникального символа в списке `chars` число
   * раз, которое он встречается. Например, вызов
   *
   *   times(List('a', 'b', 'a'))
   *
   * может вернуть следующее (порядок результирующего списка не важен):
   *
   *   List(('a', 2), ('b', 1))
   *
   * Тип `List[(Char, Int)]` обозначает список пар (кортежей), где каждая пар состоит из символа и числа.
   * Пары могут быть сконструированы с помощью скобок:
   *
   *   val pair: (Char, Int) = ('c', 1)
   *
   * Для того чтобы получить доступ к двум элементам пары, вам необходимо использовать аксессоры `_1` и `_2`:
   *
   *   val theChar = pair._1
   *   val theInt  = pair._2
   *
   * Другой способ деконструировать пару - используя pattern matching:
   *
   *   pair match {
   *     case (theChar, theInt) =>
   *       println("character is: "+ theChar)
   *       println("integer is  : "+ theInt)
   *   }
   */
    def times(chars: List[Char]): List[(Char, Int)] =
    {
      val ListChars: List[(Char, Int)] = null

      if (chars.nonEmpty){checkChar(chars.head,ListChars);times(chars.tail)}
      def checkChar(char:Char,list:List[(Char, Int)]): Int =
      {
           if(ListChars.head._1==char){ListChars.head._2+1}
        else{ checkChar(char,list.tail)}
      }
      ListChars
    }
  
  /**
   * Возвращает список узлов `Leaf` по заданной таблице частотности `freqs`.
   *
   * Возвращает список, который должен быть упорядочен по возрастанию весов (т.е.
   * голова списка должна иметь наименьший вес), где вес узла - есть частота символа.
   */
    def makeOrderedLeafList(freqs: List[(Char, Int)]): List[Leaf] = {
      val newFreqs=freqs.sortBy(_._2)
      def convert(freqs: List[(Char, Int)]): List[Leaf] = freqs match{
        case Nil => Nil
        case leaf:List[(Char, Int)] => Leaf(leaf.head._1, leaf.head._2) :: convert(leaf.tail)
      }
      convert(newFreqs)
    }
  
  /**
   * Проверяет, содержит ли список `trees` только одно кодовое дерево.
   */
    def singleton(trees: List[CodeTree]): Boolean = trees.length == 1
  
  /**
   * Параметр `trees` этой функции - список деревьев кода, упорядоченных по возрастанию весов.
   *
   * Эта функция берет два первых элемента списка `trees` и комбинирует их в отдельный узел 
   * `Fork`. Этот узел затем добавляется обратно к оставшимся элементам
   * `trees` в позицию, так чтобы сохранить порядок возрастания весов.
   *
   * Если `trees` является списком, меньше чем из двух элементов, список дожен вернуться неизменным
   */
    def combine(trees: List[CodeTree]): List[CodeTree] = {
      def combine0(forkNode: Fork, bigTrees: List[CodeTree]): List[CodeTree] = {
        if (weight(forkNode) < weight(bigTrees.head)) {
          val newTree = forkNode :: bigTrees
          newTree
        } else {
          val newTree = bigTrees.head :: combine0(forkNode, bigTrees.tail)
          newTree
        }
      }
      if (trees.length < 3) trees
      else {
        val forkNode = makeCodeTree(trees.head, trees.tail.head)
        val newTrees = trees.drop(2)
        combine0(forkNode, newTrees)
      }


    }

  
  /**
   * Эта функция будет вызвана следующим образом:
   *
   *   until(singleton, combine)(trees)
   *
   * где `trees` имеет тип `List[CodeTree]`, `singleton` и `combine` - это две функции
   * определенные выше.
   *
   * При таком вызовеIn such an invocation, `until` должен вызывать две функции до тех пор пока список
   * кодовых деревьев не будет содержать только одно дерево, и затем вернет этот список.
   *
   * Подсказка: перед написанием реализаци,
   *  - начните с определения типов параметров, таким образом, чтобы удовлетворять сигнатуре вызова сверху.
   *    Типы параметров ф-ции `until` должны совпападать с типами аргументов вызова-примера.
   *    Еще определите тип возвращаемого значения функции `until`.
   *  - постарайтесь подобрать значимые имена параметров для `xxx`, `yyy` и `zzz`.
   */
    def until(singleton: List[CodeTree] => Boolean, combine: List[CodeTree] => List[CodeTree])(tree: List[CodeTree]): List[CodeTree] = {
      if (tree.isEmpty) tree
      else if (singleton(tree)) tree
      else until(singleton, combine)(combine(tree))
    }
  
  /**
   * ЭТа функция создает дерева кода , оптимальное для кодирования текста `chars`.
   *
   * Параметр `chars` произвольный текст. Эта функция извлекает последовательность символов из текста и создает дерево кода
   * базирующегося на нем.
   */
    def createCodeTree(chars: List[Char]): CodeTree = {
      if (chars.isEmpty) null
      else{
        val list = until(singleton, combine)(makeOrderedLeafList(times(chars)))
        val tree = list.head
        tree
      }
    }
  

  // Часть 3: Декодирование

  type Bit = Int

  /**
   * Эта функция декодирует последовательность битов `bits`, используя дерево кодов `tree` и возвращает
   * результирующий список символов.
   */
    def decode(tree: CodeTree, bits: List[Bit]): List[Char] = decode0(tree,bits,tree)

  def decode0(tree: CodeTree, bits: List[Bit],node:CodeTree):List[Char]={
    node match{
      case (Leaf(c, _)) => c :: decode0(tree,bits.tail,tree)
      case (Fork(left,right,_,_)) =>
        if (bits.isEmpty)
          List()
        else if (bits.head==0)
          decode0(tree,bits,left)
        else
          decode0(tree,bits,right)

      }
    }

  
  /**
   * Дерево кодирования Хафмана для Французского языка.
   * Сгенерированно по данным из
   *   http://fr.wikipedia.org/wiki/Fr%C3%A9quence_d%27apparition_des_lettres_en_fran%C3%A7ais
   */
  val frenchCode: CodeTree = Fork(Fork(Fork(Leaf('s',121895),Fork(Leaf('d',56269),Fork(Fork(Fork(Leaf('x',5928),Leaf('j',8351),List('x','j'),14279),Leaf('f',16351),List('x','j','f'),30630),Fork(Fork(Fork(Fork(Leaf('z',2093),Fork(Leaf('k',745),Leaf('w',1747),List('k','w'),2492),List('z','k','w'),4585),Leaf('y',4725),List('z','k','w','y'),9310),Leaf('h',11298),List('z','k','w','y','h'),20608),Leaf('q',20889),List('z','k','w','y','h','q'),41497),List('x','j','f','z','k','w','y','h','q'),72127),List('d','x','j','f','z','k','w','y','h','q'),128396),List('s','d','x','j','f','z','k','w','y','h','q'),250291),Fork(Fork(Leaf('o',82762),Leaf('l',83668),List('o','l'),166430),Fork(Fork(Leaf('m',45521),Leaf('p',46335),List('m','p'),91856),Leaf('u',96785),List('m','p','u'),188641),List('o','l','m','p','u'),355071),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u'),605362),Fork(Fork(Fork(Leaf('r',100500),Fork(Leaf('c',50003),Fork(Leaf('v',24975),Fork(Leaf('g',13288),Leaf('b',13822),List('g','b'),27110),List('v','g','b'),52085),List('c','v','g','b'),102088),List('r','c','v','g','b'),202588),Fork(Leaf('n',108812),Leaf('t',111103),List('n','t'),219915),List('r','c','v','g','b','n','t'),422503),Fork(Leaf('e',225947),Fork(Leaf('i',115465),Leaf('a',117110),List('i','a'),232575),List('e','i','a'),458522),List('r','c','v','g','b','n','t','e','i','a'),881025),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u','r','c','v','g','b','n','t','e','i','a'),1486387)

  /**
   * О чем говорит секретноесообщение? Можете ли вы его декодировать?
   * Для декодирования используйте дерево Хафмана frenchCode'.
   */
  val secret: List[Bit] = List(0,0,1,1,1,0,1,0,1,1,1,0,0,1,1,0,1,0,0,1,1,0,1,0,1,1,0,0,1,1,1,1,1,0,1,0,1,1,0,0,0,0,1,0,1,1,1,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,1)

  /**
   * Напишите функцию, которая возвращает декодированный секрет
   */
    def decodedSecret: List[Char] = decode(frenchCode,secret)
  

  // Часть 4a: Кодирование с использованием дерева Хафмана

  /**
   * ЭТа функция кодирует `text` с помощью кодового дерева `tree` в
   * последовательность битов.
   */
    def encode(tree: CodeTree)(text: List[Char]): List[Bit] = text.tail match{
      case Nil => encode0(tree,text.head)
      case l: List[Char] => encode0(tree,text.head)::: encode(tree)(text.tail)
    }

  def encode0(tree: CodeTree,text: Char): List[Bit] = {
    tree match {
      case (Leaf(_, _)) => Nil
      case (Fork(left, right, chars0, _)) =>
        if (chars(left).contains(text))
          0 :: encode0(left, text)
        else if (chars(right).contains(text))
          1 :: encode0(right, text)
        else Nil
    }
  }
  
  // Часть 4b: Кодирование, использующее таблицу

  type CodeTable = List[(Char, List[Bit])]

  /**
   * ЭТа функция возвращает последовательность битов, представляющая символ `char` в
   * кодовую таблицу `table`.
   */
    def codeBits(table: CodeTable)(char: Char): List[Bit] = table.find(_._1 == char) match {
      case Some((_, l)) => l
      case None => throw new NoSuchElementException("bit sequence not found");
    }
  
  /**
   * Имя кодовое дерево, создайте кодовую таблицу, которая содержит, для каждого символа в
   * кодовом дереве, последовательность бит, представляющее этот символ.
   *
   * Подсказка: подумайте о рекурсивном решении: каждое поддерево кодового дерева `tree` - само по себе 
   * валидное кодовое дерево, которое может быть представлено в качестве кодовой таблицы. Используя кодовые таблицы
   * поддеревьев, подумайте о том, как построить кодовую таблицу для всего дерева.
   */
    def convert(tree: CodeTree): CodeTable =  {
      def rec(root: CodeTree, code: List[Bit]): CodeTable = root match {
        case Leaf(c, _) => List((c, code))
        case Fork(left, right, _, _) => mergeCodeTables(rec(left, code :+ 0), rec(right, code :+ 1))
      }
      rec(tree, List())
    }
  
  /**
   * Эта функция принимает две кодовой таблицы и делает слияние их в одну. В зависимости от того, как вы 
   * используете ее в методе `convert`, этот метод merge может также производить некоторые трансформации 
   * над двумя параметрами кодовых таблиц.
   */
    def mergeCodeTables(a: CodeTable, b: CodeTable): CodeTable = a:::b
  
  /**
   * Эта функция кодирует `text` согласно кодовому дереву `tree`.
   *
   * Для ускорения процесса кодирования, сначала она конвертирует кодовое дерево в кодовую таблицу
   * и затем использует ее для произведения непосредственно кодирования.
   */
    def quickEncode(tree: CodeTree)(text: List[Char]): List[Bit] = {
      val table = convert(tree)
      def rec(charList: List[Char], acc: List[Bit]): List[Bit] = charList match {
        case Nil => acc
        case x :: xs => rec(xs, acc ::: codeBits(table)(x))
      }
      rec(text, List())
    }
  }
