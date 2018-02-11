package dadou.mod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Driver {
	public Map<Long, Object> registry = new HashMap<>();
	Map<String, Method> cacheMethod = new HashMap<>();
	long idx = 0;
	ServerSocket serverSocket;
	Socket clientSocket;
	boolean executer = true;
	public InterfaceObject interfaceObject;

	/**
	 * @param args
	 * @throws IOException
	 */

	public Driver(InterfaceObject interfaceObject, int port) throws IOException {
		this.interfaceObject = interfaceObject;
		interfaceObject.setDriver(this);
		for (Method m : interfaceObject.getClass().getMethods()) {
			cacheMethod.put(m.getName(), m);

		}
		serverSocket = new ServerSocket(port);

	}

	public void threadWaitConnection() {
		Thread t = new Thread(() -> {
			waitConnection();

		});
		t.start();

	}

	public void waitConnection() {

		synchronized (this) {
			while (true) {
				if (clientSocket == null) {
					try {
						clientSocket = serverSocket.accept();
						return;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void executer() throws IOException {
		synchronized (this) {
			if (clientSocket == null) {
				return;
			}
		

			processSocket(clientSocket);
		}

	}

	public Object decode(Class cls, String arg) {
		if (cls == int.class) {
			return Integer.parseInt(arg);

		}
		if (cls == Integer.class) {
			return Integer.parseInt(arg);

		}
		if (cls == long.class) {
			return Long.parseLong(arg);

		}
		if (cls == Long.class) {
			return Long.parseLong(arg);

		}
		if (cls == String.class) {
			return arg;
		}
		return registry.get(Long.parseLong(arg));

	}

	public String encode(Class cls, Object ret) {
		if (cls == String.class) {
			return (String) ret;

		}
		if (cls == int.class) {
			return String.valueOf((Integer) ret);

		}
		if (cls == Integer.class) {
			return String.valueOf((Integer) ret);

		}
		if (cls == long.class) {
			return String.valueOf((Long) ret);

		}
		if (cls == Long.class) {
			return String.valueOf((Long) ret);

		}
		if (cls == void.class) {
			return "null";
		}
		registry.put(idx, ret);
		String r = String.valueOf(idx);
		idx++;
		return r;

	}

	public void processSocket(Socket clientSocket) {
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			while (this.interfaceObject.isRuning()) {
				String string = in.readLine();
				if (string != null) {
					Method m = this.cacheMethod.get(string);
					System.out.println("debut " + string);
					Object args[] = new Object[m.getParameterTypes().length];
					int i = 0;
					for (Class cls : m.getParameterTypes()) {
						System.out.println(" wait " + cls + " (" + i
								+ ") for  " + string);
						String arg = in.readLine();
						System.out.println(" read " + arg);
						args[i] = this.decode(cls, arg);

						i++;
					}

					Object r = m.invoke(interfaceObject, args);
					String rs = this.encode(m.getReturnType(), r);
					out.print(rs);
					System.out.println(" reponse " + rs);

					out.flush();
				}

			}
		} catch (IOException e) {
			clientSocket = null;

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("executer ");
	}

}
