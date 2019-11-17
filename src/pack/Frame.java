package pack;


/**
 * Frame Class - allows the program to instantiate runtime frames for the function calls. 
 * All the Frames contain a stack of operands, a memory array, a caller frame and the program counter.
 * Each frame is instantiated and placed onto the runTime stack in VM.java and executed through the 
 * method run. This method iterates through the program instruction array and performs actions according
 * to the semantics below:
 * 
    # 	push value onto operand stack
		->	iconst k : push integer constant k onto stack
		->	iload k : push integer at address k in variable area onto stack
		->	fconst x : push floating-point constant x onto stack
		->	fload k : push floating-point value at address k in variable area onto stack
	# 	pop top of operand stack and store

		->	istore k : pop top of stack, which is assumed to be an integer, and store it in address k in variable area
		->	fstore k : pop top of stack, which is assumed to be a floating-point number, and store it in address k in variable area

	# 	arithmetic
		->	iadd, isub, imul, idiv : pop top two integer values from stack, apply operator to stack[top-1] and stack[top], push result onto stack
		->	fadd, fsub, fmul, fdiv : pop top two floating-point values from stack, apply operator to stack[top-1] and stack[top], push result onto stack

	# 	type conversion
		->	intToFloat : convert integer at top of stack to floating-point number

	# 	comparison-jump
		->	icmpeq k :   pop top two integer values from stack; 
			if stack[top-1] = stack[top] then goto instruction at address k
		->	icmpne k :   pop top two integer values from stack; 
			if stack[top-1] != stack[top] then goto instruction at address k
		->	icmplt k :   pop top two integer values from stack; 
			if stack[top-1] < stack[top] then goto instruction at address k
		->	icmple k :   pop top two integer values from stack; 
			if stack[top-1] <= stack[top] then goto instruction at address k
		->	icmpgt k :   pop top two integer values from stack; 
			if stack[top-1] > stack[top] then goto instruction at address k
		->	icmpge k :   pop top two integer values from stack; 
			if stack[top-1] >= stack[top] then goto instruction at address k
		->	Likewise for fcmpeq, fcmpne, fcmplt, fcmple, fcmpgt, fcmpge

	# 	unconditional jump
		->	goto k : go to instruction at address k

	# 	function invocation
		->	invoke k1, k2, k3 : invoke function code that:
		 	starts at label k1, k2 = the # of parameters, k3 = the # of local variables

	# 	function return
		->	return : return from void-type function
		->	ireturn : return from function whose return type is integer
		->	freturn : return from function whose return type is floating-point

	# 	print
		->	print k : print value at address k in variable area on the screen

 *************************************************************************************************************
		For more information about the methods, please check their javadocs
 *************************************************************************************************************

 *************************************************************************************************************
 *                      @author Vagner Machado - QC ID 23651127 - Fall 2019
 *************************************************************************************************************
 */
public class Frame extends VM
{
	
	private  Object [] memory;
	private int returnAddress;
	private int programCounter;
	private Frame callerFrame;

	/**
	 * Frame constructor - instantiates a Frame to be placed on the runtime Stack
	 * @param pc - the program counter
	 * @param os - the operand stack
	 * @param mem - the memory needed for the frame local vars and param
	 * @param c - the caller frame
	 */
	public Frame(int pc, Object []  mem , int add, Frame f)
	{
		callerFrame = f;
		returnAddress = add;
		memory = mem;
		programCounter = pc;
	}
	
	/**
	 * default constructor
	 */
	public Frame()
	{
		
	}

	/**
	 * run - performs all actions required to execute the instructions 
	 * according to the semantics using the instance variables: stack
	 * of operands, memory cells, callee frame and program counter.
	 * The method iterates through the instruction array
	 * and emit valid, push, pop, compare, invoke and other instructions based on 
	 * contents of array of instructions and given semantics.
	 */
	protected void run()
	{
		//while there are instructions
		while(programCounter < arrayLocation)
		{
			//get the instructions and process it as dictated by semantics
			Instruction i = instructionArray[programCounter];
			//System.out.println(i.print() + "\t - pc " + programCounter + " stack " + operandStack.size());
			if(i instanceof Iconst || i instanceof Fconst)
			{
				VM.operandStack.push(i.getValue());
				programCounter++;
			}

			else if(i instanceof Iload || i instanceof Fload)
			{
				VM.operandStack.push(memory[(int) i.getValue()]);
				programCounter++;
			}

			else if(i instanceof Istore || i instanceof Fstore)
			{
				memory[(int) i.getValue()] = VM.operandStack.pop();
				programCounter++;
			}

			else if(i instanceof Iadd)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				VM.operandStack.push(bottom + top);
				programCounter++;
			}

			else if(i instanceof Fadd)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				VM.operandStack.push(bottom + top);
				programCounter++;
			}

			else if(i instanceof Isub)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				VM.operandStack.push(bottom - top);
				programCounter++;
			}

			else if(i instanceof Fsub)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				VM.operandStack.push(bottom - top);
				programCounter++;
			}

			else if(i instanceof Imul)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				VM.operandStack.push(bottom * top);
				programCounter++;
			}

			else if(i instanceof Fmul)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				VM.operandStack.push(bottom * top);
				programCounter++;
			}

			else if(i instanceof Idiv)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				VM.operandStack.push(bottom / top);
				programCounter++;
			}

			else if(i instanceof Fdiv)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				VM.operandStack.push(bottom / top);
				programCounter++;
			}

			else if (i instanceof Print)
			{
				System.out.println("Print: " + memory[(int) i.getValue()]);
				programCounter++;
			}	

			else if (i instanceof IntToFloat)
			{
				VM.operandStack.push((double)((int)VM.operandStack.pop()) + 0.0);
				programCounter++;
			}

			else if (i instanceof Goto)
			{
				programCounter = (int) i.getValue();
			}

			else if (i instanceof Icmpeq)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				if(bottom == top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmpeq)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				if(bottom == top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmpne)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				if(bottom != top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmpne)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				if(bottom != top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmplt)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				if(bottom < top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmplt)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				if(bottom < top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmple)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				if(bottom <= top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmple)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				if(bottom <= top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmpgt)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				if(bottom > top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmpgt)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				if(bottom > top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmpge)
			{
				int top = (int) VM.operandStack.pop();
				int bottom = (int) VM.operandStack.pop();
				if(bottom >= top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmpge)
			{
				double top = (double) VM.operandStack.pop();
				double bottom = (double) VM.operandStack.pop();
				if(bottom >= top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Return)
			{
				callerFrame.programCounter = returnAddress;
				runtimeStack.pop();
				return;
			}

			else if (i instanceof Ireturn)
			{
				int value = (int) VM.operandStack.pop();
				returnValueToCaller(value);
				callerFrame.programCounter = returnAddress;
				runtimeStack.pop();
				return;
			}

			else if (i instanceof Freturn)
			{
				double value = (double)VM.operandStack.pop();
				returnValueToCaller(value);
				callerFrame.programCounter = returnAddress;
				runtimeStack.pop();
				return;
			}

			else if (i instanceof Invoke)
			{
				Integer [] val = (Integer[]) i.getValue();
				Object [] paramAndMemory = new Object[val[1] + val[2]]; 
				int param = val[1];	
				//int oldCounter = programCounter;
				//programCounter = val[0];
				//System.out.println("params " + param + " - stack size: " + operandStack.size());
				while(param > 0)
				{
					paramAndMemory[--param] = VM.operandStack.pop();
				}
				runtimeStack.push(new Frame(val[0], paramAndMemory, programCounter + 1, this));
				runtimeStack.peek().run(); //processes the stack Frame
				

			}
		}
	}

	/**
	 * returnedValueToCaller - allows a callee frame to push return value onto caller stack of operands
	 * @param valueFromCalee - callee return value to be pushed to this stack of operands
	 */
	private void returnValueToCaller(Object valueFromCalee)
	{
		VM.operandStack.push(valueFromCalee);
	}
}