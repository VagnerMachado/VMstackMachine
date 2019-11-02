package pack;

import java.util.Stack;

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
	private  Stack<Object> operandStack;
	private  Object [] memory;
	private Frame caller;
	private int programCounter;

	/**
	 * Frame constructor - instantiates a Frame to be placed on the runtime Stack
	 * @param pc - the program counter
	 * @param os - the operand stack
	 * @param mem - the memory needed for the frame local vars and param
	 * @param c - the caller frame
	 */
	public Frame(int pc, Stack<Object> os, Object []  mem , Frame c)
	{
		caller = c;
		memory = mem;
		operandStack = os;
		programCounter = pc;
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
			
			if(i instanceof Iconst || i instanceof Fconst)
			{
				operandStack.push(i.getValue());
				programCounter++;
			}

			else if(i instanceof Iload || i instanceof Fload)
			{
				operandStack.push(memory[(int) i.getValue()]);
				programCounter++;
			}

			else if(i instanceof Istore || i instanceof Fstore)
			{
				memory[(int) i.getValue()] = operandStack.pop();
				programCounter++;
			}

			else if(i instanceof Iadd)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				operandStack.push(bottom + top);
				programCounter++;
			}

			else if(i instanceof Fadd)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				operandStack.push(bottom + top);
				programCounter++;
			}

			else if(i instanceof Isub)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				operandStack.push(bottom - top);
				programCounter++;
			}

			else if(i instanceof Fsub)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				operandStack.push(bottom - top);
				programCounter++;
			}

			else if(i instanceof Imul)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				operandStack.push(bottom * top);
				programCounter++;
			}

			else if(i instanceof Fmul)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				operandStack.push(bottom * top);
				programCounter++;
			}

			else if(i instanceof Idiv)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				operandStack.push(bottom / top);
				programCounter++;
			}

			else if(i instanceof Fdiv)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				operandStack.push(bottom / top);
				programCounter++;
			}

			else if (i instanceof Print)
			{
				System.out.println("Print: " + memory[(int) i.getValue()]);
				programCounter++;
			}	

			else if (i instanceof IntToFloat)
			{
				operandStack.push((double)((int)operandStack.pop()) + 0.0);
				programCounter++;
			}

			else if (i instanceof Goto)
			{
				programCounter = (int) i.getValue();
			}

			else if (i instanceof Icmpeq)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				if(bottom == top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmpeq)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				if(bottom == top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmpne)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				if(bottom != top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmpne)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				if(bottom != top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmplt)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				if(bottom < top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmplt)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				if(bottom < top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmple)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				if(bottom <= top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmple)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				if(bottom <= top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmpgt)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				if(bottom > top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmpgt)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				if(bottom > top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Icmpge)
			{
				int top = (int) operandStack.pop();
				int bottom = (int) operandStack.pop();
				if(bottom >= top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Fcmpge)
			{
				double top = (double) operandStack.pop();
				double bottom = (double) operandStack.pop();
				if(bottom >= top)
					programCounter = (int) i.getValue();
				else
					programCounter++;			
			}

			else if (i instanceof Return)
			{
				runtimeStack.pop();
				return;
			}

			else if (i instanceof Ireturn)
			{
				returnValueToCaller(operandStack.pop());
				runtimeStack.pop();
				return;
			}

			else if (i instanceof Freturn)
			{
				returnValueToCaller(operandStack.pop());
				runtimeStack.pop();
				return;
			}

			else if (i instanceof Invoke)
			{
				Integer [] val = (Integer[]) i.getValue();
				Object [] paramAndMemory = new Object[val[1] + val[2]]; 
				int param = val[1];			
				while(param > 0)
				{
					paramAndMemory[--param] = operandStack.pop();
				}
				runtimeStack.push(new Frame(val[0], new Stack<Object>(), paramAndMemory, this));
				runtimeStack.peek().run(); //processes the stack Frame
				programCounter++;
			}
		}
	}
	
	/**
	 * returnedValueToCaller - allows a callee frame to push return value onto caller stack of operands
	 * @param valueFromCalee - callee return value to be pushed to this stack of operands
	 */
	private void returnValueToCaller(Object valueFromCalee)
	{
		caller.operandStack.push(valueFromCalee);
	}
}