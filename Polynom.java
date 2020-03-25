import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Polynom {
	
	private Vector<Integer> polynom;
	private int r;

	
public Polynom() {
	polynom = new Vector<Integer>();
}

Polynom(int deg, int auto){ //Создание многочлена с необходимой степенью либо с вводом коэффициентов, либо нулевого
	polynom = new Vector<Integer>();
	polynom.setSize(deg);
    if (auto == 0) {
        System.out.println("Введите коэффициенты многочлена: ");
        int tmp = 0;
        Scanner s = new Scanner(System.in);
        for (int i = 0; i < deg; i++) {
            System.out.print("Для " + (deg - i - 1) + " степени ");
            tmp = s.nextInt();
            if (tmp % 2 == 0) {
                polynom.set(i, 0);
            } else {
                polynom.set(i, 1);
            }
        }
    }
    else if (auto == 1){
        for (int i = 0; i < deg; i++){
            polynom.set(i, 0);
        }
    }
}

public Polynom(int flag)  {//Cоздание многочлена с рандомными коэффициентами или ввод коэффициентов.
	Scanner input = new Scanner(System.in);
	
	/*Ввод степени пораждающего многочлена*/
	System.out.println("Введите deg(g(x)): " + "Пример: 4 - значит макс степень = x^3, т.е. deg - 1");
	String tempString = input.nextLine(); //Считывание введенных значений с консоли.
	r = Integer.parseInt(tempString); //Перевод из String to int
	/*====================================*/
	
	polynom = new Vector<Integer>(r);
	
	if(flag == 1) { //rand
		
		Random rand = new Random(); //instance of random class
		
		 for(int i = 0; i < r; i++) {
		
			 polynom.add(rand.nextInt(2)); //rand.nextInt(1) - generate random values from 0-1
			 
		 }
		
	}
	else if(flag == 2) {//vvod
		
		System.out.println("Введите значения многочлена: ");
		
		for(int i = 0; i < r; i++) {
		
			tempString = input.nextLine();
			int znachenie = Integer.parseInt(tempString);
			
			if(znachenie != 1 && znachenie != 0) {
				System.out.println("Entered values must be either 1 or 0!");
				System.exit(0);
			}
			
			polynom.add(znachenie);
	
		}
	}
	else {
		
		System.out.println("error");
		System.exit(0);
		
	}
	
	
}

public Polynom(Vector<Integer> vector) {//Создание полинома по известному нам вектору
	polynom = new Vector<Integer>(vector.size());
	for(int i = 0; i < vector.size(); i++) {
		polynom.add(vector.get(i));
	}
}

public Polynom encrypt(Polynom m, Polynom g) {
	
	Polynom ax = new Polynom();
	Polynom cx = new Polynom();
	
	
	System.out.println("Промежуточные значения при получение a(x): ");
	
	Polynom m2 = m.multiplication(g.getSize()-1, g);
	System.out.println("m(x)*x^r: " + m2 + "[Standart form: " + m2.standartForm() + "]");
	cx = g.mod(m2, m);
	System.out.println("cx: " + cx + "[Standart form: " + cx.standartForm() + "]");
	ax = m2.sum(cx);
	
	return ax;
}

public Polynom channel(Polynom ax, Polynom e) { //ax проходит через канал, т.е. наложение вектора ошибок
	
	if(e.getSize() > ax.getSize()) { //если вектор ошибок больше, чем ax, то обрубаем е(х) до нужного размера
		
		Polynom tmp = new Polynom();
		
		for(int i = 0; i < e.getSize()- ax.getSize(); i++) {
			
			tmp.add(e.get(i));
			
		}
		e = tmp;
		
	}
	
	if (ax.getSize() - e.getSize() > 0) { //если ax больше вектора ошибок, то дополняем вектор ошибок.
		
		while (e.getSize() != ax.getSize()) {
			
			e.add(0);
			
		}
	}
	
	ax = ax.sum(e);

	return ax;
}

public int decrypt(Polynom bx, Polynom g) { //Алгоритм декодирования
	
	Polynom test = new Polynom();
	test = g.mod(bx, g); //делим bx на gx
	
	System.out.println("=======================\nПромежуточные значения при декодировании: ");
	System.out.println("Остаток от bx mod gx: " + test + "[Standart form: " + test.standartForm() + "]");
	for (int i = 0; i < test.getSize(); i++) { //если в результате деления получился остаток, то возвращается 1
		
		if (test.get(i) == 1) {
			
			return 1;
		
		}
	}
	return 0; //если по итогу получился нулевой вектор - ошибок нет.
}

public int alterDecryptDOP(Polynom bx, Polynom g) {
	
	int r = g.getSize() - 1;
	int informationM = bx.getSize() - r; //длина инф. блока

	Polynom mb = new Polynom();
	
	for(int i = 0; i < informationM; i++) { //вычисление инф. части
		mb.add(bx.get(i));
	}
	
	Polynom cb = new Polynom();
	for(int i = informationM; i < bx.getSize(); i++) { //вычисление КС
		cb.add(bx.get(i));
	}
	
	System.out.println("=======================\nПромежуточные значения при декодировании альтернативным способом: ");
	System.out.println("Информационная часть (mb) сообщения b(x): " + mb + "[Standart form: " + mb.standartForm() + "]");
	System.out.println("Контрольная сумма (cb) сообщения b(x): " + cb + "[Standart form: " + cb.standartForm() + "]");
	
	
	Polynom bDerivative = new Polynom();
			bDerivative = mb.encrypt(mb, g); //Прогоняем через кодер
	Polynom cbDerivative = new Polynom();
	Polynom mbDerivative = new Polynom();
	for(int i = 0; i < informationM; i++) { //вычисление инф. части'
		mbDerivative.add(bDerivative.get(i));
	}
	for(int i = informationM; i < bDerivative.getSize(); i++) { //вычисление КС'
		cbDerivative.add(bDerivative.get(i));
	}
	
	System.out.println("Новая информациооная (mb')часть b(x): " + mbDerivative + "[Standart form: " + mbDerivative.standartForm() + "]");
	System.out.println("Новая контрольная сумма (cb')сообщения b(x): " + cb + "[Standart form: " + cb.standartForm() + "]");
	for (int i = 0; i < cb.getSize(); i++) { //если в результате деления получился остаток, то возвращается 1
		
		if (cbDerivative.get(i) != cb.get(i)) {
			
			return 1;
		
		}
	}
	return 0; //если по итогу получился нулевой вектор - ошибок нет.
	
}

public Polynom mod(Polynom arr, Polynom m){ //Деление
    int tmp = 0;
    Polynom m2 = new Polynom(arr.getSize(), 1);
    for (int i = 0; i < m2.getSize(); i++){
        m2.set(i, arr.get(i));
    }
    Polynom g2 = new Polynom(arr.polynom.size(), 1);
    while (true){
        for (int i = 0; i < m2.getSize(); i++){
            if (m2.get(i) != 0){
                tmp = m2.getSize() - 1 - i;
                break;
            }
            else{
                tmp = 0;
            }
        }
        if (tmp < getSize() - 1){
            break;
        }
        tmp = tmp - getSize() + 1;
        g2 = multiplication(tmp, m); // вычитаемое
        for (int i = 0; i < m2.getSize(); i++){
            m2.set(i, (m2.get(i) + g2.get(i)) % 2);
        }
    }
    return  m2;
}

public Polynom multiplication(int tmp, Polynom sec){//умножение
    int size = polynom.size() + sec.polynom.size() - 1;//вычисление макс. степени получаемого многочлена
    Polynom tmp2 = new Polynom(size, 1); 
    for (int i = 0; i < polynom.size(); i++){
        if (polynom.get(i) != 0){
            tmp2.set(i + sec.polynom.size() - 1 - tmp, 1);
        }
        else{
            tmp2.set(i + sec.polynom.size() - 1 - tmp, 0);
        }
    }
    return tmp2;
}

public Polynom sum(Polynom sec){ //XOR
    Polynom tmp = new Polynom(getSize(), 1);
    for (int i = 0; i < getSize(); i++){
        tmp.set(i, (get(i) + sec.get(i)) % 2);
    }
    return tmp;
}


public void set(int i, int k){ //Установка значения k полиному на позиции i 
    polynom.set(i, k);
}

public int getSize() { //returns polynomial size 
	return polynom.size();
}

public int get(int index) { //Получение значения по индексу i
	return polynom.get(index);
}

public int getR() { //Получение степени
	return r;
}

public void add(int i) { //add element to polynom
	polynom.add(i);
}


@Override
public String toString(){ //Вывод полинома формата "x^6+x+1"
	StringBuilder sb = new StringBuilder();
	for(int i = 0, j = polynom.size()-1; i < polynom.size() - 2; i++, j--) {
		if(polynom.get(i).equals(0)) {
			sb.append("");
		}
		if(polynom.get(i).equals(1)){
			sb.append("x^" + j + "+");	
		}
	}
	if(polynom.get(polynom.size()-2).equals(1)) {
		sb.append("x+");
	}
	if(polynom.get(polynom.size()-1).equals(1)) {
		sb.append("1");
	}
	return sb.toString();
}

public String standartForm() { //Вывод полинома формата"1010"
	StringBuilder sb = new StringBuilder();
	for(int i = 0; i < polynom.size(); i++) {
		sb.append(polynom.get(i));
	}
	return sb.toString();
}



}