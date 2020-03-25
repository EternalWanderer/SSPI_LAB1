import java.util.Scanner;
import java.util.Vector;

public class Main {
	
	public static void main(String[] args) {
	Scanner input = new Scanner(System.in);
	int k; 
	int l;
	String tempString;
	//Способ создания g(x)//
	System.out.println("Выберите выбор ввода пораждающего многочлена g(x)\n1 - Случайное создание \n2 - Ввод коэфф");
	tempString = input.nextLine();
	int flag = Integer.parseInt(tempString);
	Polynom g = new Polynom(flag);
	System.out.println("Получившийся пораждающий многочлен g(x): " + g);
	System.out.println("Другая форма представления многочлена g(x): " + g.standartForm() + "\n=====================");
	//===================================//
	
	//Ввод k//
	System.out.println("Введите k:");
	tempString = input.nextLine();
	k = Integer.parseInt(tempString);
	//===================================//
	
	//Способ создания вектора ощибок e(x)//
	System.out.println("Выберите выбор ввода вектора ошибок\n1 - Случайное создание \n2 - Ввод коэфф");
	tempString = input.nextLine();
	flag = Integer.parseInt(tempString);
	Polynom e = new Polynom(flag);
	System.out.println("Получившийся вектор ошибок e(x): " + e);
	System.out.println("Другая форма представления многочлена e(x): " + e.standartForm()+ "\n=====================");
	//===================================//
	
	//Способ создания информационной последовательности m(x)//
	System.out.println("Выберите выбор ввода информационного сообщения\n1 - Случайное создание \n2 - Ввод коэфф");
	tempString = input.nextLine();
	flag = Integer.parseInt(tempString);
	Polynom m = new Polynom(flag);
	System.out.println("Получившийся вектор информационной последовательности m(x): " + m);
	System.out.println("Другая форма представления многочлена m(x): " + m.standartForm()+ "\n=====================");
	//===================================//
	
	//===================================//	
	l = m.getSize();
	
	if (l < k) {
		while (m.getSize() < k) { 
			m.add(0);			//Расширение m до k бит
		}
	} else {
		if (l > k) {
			Polynom tmp = new Polynom();
			for (int i = m.getSize() - k; i < m.getSize(); i++) { // Обрезание m до младших k бит
				tmp.add(m.get(i));
			}
			m = tmp;
		}
	}
	//===================================//
	 
	//Создание полинома a(x)//
	Polynom ax = new Polynom();
	ax = ax.encrypt(m, g);
	System.out.println("Получившийся вектор a(x) на выходе канала : " + ax);
	System.out.println("Другая форма представления многочлена a(x): " + ax.standartForm()+ "\n=====================");
	//===================================//
	 
	//Создание полинома b(x)// 
	Polynom bx = new Polynom();
	bx = bx.channel(ax, e);
	System.out.println("Получившийся вектор b(x): " + bx);
	System.out.println("Другая форма представления многочлена b(x): " + bx.standartForm()+ "\n=====================");
	//===================================//
	 
	//Проверка вектора b(x)//
	System.out.println("Результат декодирования E:" + bx.decrypt(bx, g)+ "\n=====================");
	System.out.println("Результат альтернативного декодирования E:" + bx.alterDecryptDOP(bx, g)+ "\n=====================");
	
	 
/*	Vector<Integer> m1 = new Vector<Integer>();
	m1.add(1);
	m1.add(0);
	m1.add(1);
	m1.add(0);

	Vector<Integer> g1 = new Vector<Integer>();
	g1.add(1);
	g1.add(0);
	g1.add(1);
	g1.add(1);
	Vector<Integer> e1 = new Vector<Integer>();
	e1.add(0);
	e1.add(0);
	e1.add(0);
	e1.add(1);
	e1.add(0);
	e1.add(1);
	e1.add(1);
	Polynom m = new Polynom(m1);
	Polynom g = new Polynom(g1);
	Polynom e = new Polynom(e1);
	
	
	Polynom ax = new Polynom();
	ax = ax.encrypt(m, g);
	System.out.println("!!!ax:" +ax + " "+ ax.standartForm()); 
	
	
	Polynom bx = new Polynom();
	bx = bx.channel(ax, e);
	
	System.out.println("!!!bx:" +bx + " "+ bx.standartForm()); 
	
	System.out.println("Result: " +  bx.decrypt(bx, g));
	
	System.out.println("Result2: " + bx.alterDecryptDOP(bx, g)); */
	}
	
}
