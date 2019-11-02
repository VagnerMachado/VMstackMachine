package pack;

import java.util.Stack;

/**
 * Frame Class 
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

	public Frame(int pc, Stack<Object> os, Object []  mem , Frame c)
	{
		caller = c;
		memory = mem;
		operandStack = os;
		programCounter = pc;
	}
	
	/**
	 * receiveReturnedValue - allows a callee frame to push a value onto caller stack of operands
	 * @param valueFromCalee - callee return value to be pushed to this stack of operands
	 */
	private void receiveReturnedValue(Object valueFromCalee)
	{
		operandStack.push(valueFromCalee);
	}
	
	/**
	 * run - performs all actions required to execute the invoke call
	 * using a stack of operands and memory cells, the method iterates through the instruction array
	 * and emit valid, push, pop, compare, invoke and other instructions based on 
	 * contents of array of instructions. 
	 */
	protected void run()
	{
		

		while(programCounter < arrayLocation)
		{
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
				caller.receiveReturnedValue(operandStack.pop());
				runtimeStack.pop();
				return;
			}

			else if (i instanceof Freturn)
			{
				caller.receiveReturnedValue(operandStack.pop());
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
}