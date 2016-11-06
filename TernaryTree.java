import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.*;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;
 public class TernaryTree<T> implements TernaryTreeInterface<T>
{
    private T rootData;
    private TernaryNode<T> root;
    
    public TernaryTree()
    {
        root = null;
    }
    
    public TernaryTree(T rootData)
    {
        root = new TernaryNode<T>();
        setTree(rootData);
    }
    
    public TernaryTree(T rootData, TernaryTree<T> leftTree, TernaryTree<T> middleTree, TernaryTree<T> rightTree)
    {
        privateSetTree(rootData,leftTree,middleTree,rightTree);
    }
    
    /**
     * Sets the ternary tree to a new one-node ternary tree with the given data
     * @param rootData The data for the new tree's root node
     */
    public void setTree(T rootData)
    {
        root.setData(rootData);
    }
    
    /**
     * Sets this ternary tree to a new ternary tree
     * @param rootData The data for the new tree's root node
     * @param leftTree The left subtree of the new tree
     * @param middleTree The middle subtree of the new tree
     * @param rightTree The right subtree of the new tree
     */
    public void setTree(T rootData, TernaryTreeInterface<T> leftTree, TernaryTreeInterface<T> middleTree, TernaryTreeInterface<T> rightTree)
    {
        privateSetTree(rootData, (TernaryTree<T>)leftTree, (TernaryTree<T>)middleTree, (TernaryTree<T>)rightTree);
    }
    
    private void privateSetTree(T rootData, TernaryTree<T> leftTree, TernaryTree<T> middleTree, TernaryTree<T> rightTree)
    {
        root = new TernaryNode<T>(rootData);
        if(leftTree != null)
        {
            root.setLeftChild(leftTree.root);
        }
        if(middleTree != null)
        {
            root.setMiddleChild(middleTree.root);
        }
        if(rightTree != null)
        {
            root.setRightChild(rightTree.root);
        }
    }
    
    /**
     * Gets the data in the root node
     * @return the data from the root node
     * @throws EmptyTreeException if the tree is empty
     */
    public T getRootData()
    {
        if(!isEmpty())
        {
            return root.getData();
        }
        else
        {
            throw new EmptyTreeException("Tree is empty and has no root");
        }
    }
    
    public TernaryNode<T> getRoot()
    {
        return root;
    }

    /**
     * Gets the height of the tree (i.e., the maximum number of nodes passed through from root to
     * leaf, inclusive)
     * @return the height of the tree
     */
    public int getHeight()
    {
        int height = 1;
        int leftHeight = root.getLeftChild().getHeight();
        int middleHeight = root.getMiddleChild().getHeight();
        int rightHeight = root.getRightChild().getHeight();
        if(leftHeight > rightHeight && leftHeight > middleHeight)
        {
            height += leftHeight;
        }
        else if(middleHeight > leftHeight && middleHeight > rightHeight)
        {
            height += middleHeight;
        }
        else
        {
            height += rightHeight;
        }
        return height;
    }

    /**
     * Counts the total number of nodes in the tree
     * @return the number of nodes in the tree
     */
    public int getNumberOfNodes()
    {        
        int counter = 0;
        Iterator<T> i = this.getPreorderIterator();
        while(i.hasNext())
        {
            counter++;
            i.next();
        }
        return counter;
    }

    /**
     * Determines whether the tree is empty (i.e., has no nodes)
     * @return true if the tree is empty, false otherwise
     */
    public boolean isEmpty()
    {
        return root == null;
    }

    /**
     * Removes all data and nodes from the tree
     */
    public void clear()
    {
        root = null;
    }
    
     /**
     * Creates an iterator to traverse the tree in preorder fashion
     * @return the iterator
     */
    public Iterator<T> getPreorderIterator()
    {
        return new PreorderIterator();
    }

    /**
     * Creates an iterator to traverse the tree in postorder fashion
     * @return the iterator
     */
    public Iterator<T> getPostorderIterator()
    {
       return new PostorderIterator();
    }

    /**
     * Creates an iterator to traverse the tree in inorder fashion
     * @return the iterator
     */
    //Inorder traversal is not supported by a ternary tree because inorder traversals
    //move from the tree's leftmost node, to it's rightmost node. With a ternary
    //tree, there is a middle child that does not allow this process to happen, as a middle child
    //is in the same left-to-right position as it's parent, so neither the parent or the child would
    //necessarily be traversed before the other.
    public Iterator<T> getInorderIterator()
    {
        throw new UnsupportedOperationException("Inorder Iterator is not supported");
    }

    /**
     * Creates an iterator to traverse the tree in level-order fashion
     * @return the iterator
     */
    public Iterator<T> getLevelOrderIterator()
    {
       return new LevelOrderIterator();
    }
   
    private class PreorderIterator implements Iterator<T>
    {
        private Stack<TernaryNode<T>> nodeStack;

        public PreorderIterator() 
        {
            nodeStack = new Stack<>();
            if (root != null) 
            {
                nodeStack.push(root);
            }
        } // end default constructor

        public boolean hasNext() 
        {
            return !nodeStack.isEmpty();
        } // end hasNext
        
        public Stack<TernaryNode<T>> getStack()
        {
            return nodeStack;
        }

        public T next() 
        {
            TernaryNode<T> nextNode;
            if (hasNext()) 
            {
                nextNode = nodeStack.pop();
                TernaryNode<T> leftChild = nextNode.getLeftChild();
                TernaryNode<T> middleChild = nextNode.getMiddleChild();
                TernaryNode<T> rightChild = nextNode.getRightChild();

                // Push into stack in reverse order of recursive calls
                if (rightChild != null) 
                {
                    nodeStack.push(rightChild);
                }
                
                if(middleChild != null)
                {
                    nodeStack.push(middleChild);
                }

                if (leftChild != null) 
                {
                    nodeStack.push(leftChild);
                }
            } 
            else 
            {
                throw new NoSuchElementException("Traversal is complete");
            }
            
            return nextNode.getData();
        } // end next

        public void remove() 
        {
            throw new UnsupportedOperationException();
        } // end remove
    }
   
    private class PostorderIterator implements Iterator<T>
    {
        private Stack<TernaryNode<T>> nodeStack;
        private TernaryNode<T> currentNode;
        
        public PostorderIterator()
        {
            nodeStack = new Stack<>();
            currentNode = root;
        }
       
        public boolean hasNext()
        {
            return !nodeStack.isEmpty() || (currentNode != null);
        }
       
        public T next()
        {
            boolean foundNext = false;
            TernaryNode<T> nextNode = null;
            TernaryNode<T> leftChild = null;
            TernaryNode<T> middleChild = null;
            TernaryNode<T> rightChild = null;
            while(currentNode != null)
            {
                nodeStack.push(currentNode);
                leftChild = currentNode.getLeftChild();
                if(leftChild ==  null && middleChild == null)
                {
                    currentNode = currentNode.getRightChild();
                }
                else if(leftChild == null)
                {
                    currentNode = currentNode.getMiddleChild();
                }
                else
                {
                    currentNode = leftChild;
                } 
            }
            if(!nodeStack.isEmpty())
            {
                nextNode = nodeStack.pop();
                TernaryNode<T> parent = null;
                if(!nodeStack.isEmpty())
                {
                    parent = nodeStack.peek();
                    if(nextNode == parent.getLeftChild())
                    {
                        currentNode = parent.getMiddleChild();
                    }
                    else if(nextNode == parent.getMiddleChild())
                    {
                        currentNode = parent.getRightChild();
                    }
                    else
                    {
                        currentNode = null;
                    }
                }
                else
                {
                    currentNode = null;
                }
            }
            else
            {
                throw new NoSuchElementException("Traversal is complete");
            }
            return nextNode.getData();
        }
       
        public void remove()
        {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }
   
    private class LevelOrderIterator implements Iterator<T>
    {
         private LinkedList<TernaryNode<T>> nodeQueue;
 
         public LevelOrderIterator()
         {
             nodeQueue = new LinkedList<TernaryNode<T>>();
             if(root != null)
             {
                 nodeQueue.add(root);
             }
         }
       
         public boolean hasNext()
         {
             return !nodeQueue.isEmpty();
         }
        
         public T next()
         {
             TernaryNode<T> nextNode;
             if (hasNext()) 
             {
                 nextNode = nodeQueue.poll();
                 TernaryNode<T> leftChild = nextNode.getLeftChild();
                 TernaryNode<T> middleChild = nextNode.getMiddleChild();
                 TernaryNode<T> rightChild = nextNode.getRightChild();
 
                 if (leftChild != null) 
                 {
                     nodeQueue.add(leftChild);
                 }
                 
                 if(middleChild != null)
                 {
                     nodeQueue.add(middleChild);
                 }

                 if (rightChild != null) 
                 {
                     nodeQueue.add(rightChild);
                 }
             } 
             else 
             {
                 throw new NoSuchElementException("Traversal is complete");
             }
 
             return nextNode.getData();
         }
        
         public void remove()
         {
             throw new UnsupportedOperationException("Remove is not supported");
         }
    }
}
