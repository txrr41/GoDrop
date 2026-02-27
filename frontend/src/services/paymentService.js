import { loadStripe } from '@stripe/stripe-js'

// Chave pública do Stripe - deve ser configurada via variável de ambiente em produção
const STRIPE_PUBLIC_KEY = import.meta.env.VITE_STRIPE_PUBLIC_KEY || 'pk_test_51SvVp38HCfbXm3tKlj7cQF204tYrkDYYpBCt9LUau0QazoqVDwXgh2OY51VoDTeTvJMjl45ureNNIp9pHxsSTbCT00cn3G5bYe'
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

let stripePromise = null

export const getStripe = () => {
    if (!stripePromise) {
        stripePromise = loadStripe(STRIPE_PUBLIC_KEY)
    }
    return stripePromise
}

class PaymentService {

    async createPaymentIntent(orderData) {
        try {
            console.log('📤 Criando Payment Intent...', orderData)

            const response = await fetch(`${API_URL}/api/payments/create`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(orderData)
            })

            if (!response.ok) {
                const text = await response.text()
                throw new Error(text || 'Erro ao criar pagamento')
            }

            const data = await response.json()
            console.log('✅ Payment Intent criado:', data)

            return data

        } catch (error) {
            console.error('❌ Erro ao criar Payment Intent:', error)
            throw error
        }
    }

    async confirmCardPayment(clientSecret, cardElement, billingDetails) {
        try {
            console.log('Confirmando pagamento com cartão...')

            const stripe = await getStripe()

            const { error, paymentIntent } = await stripe.confirmCardPayment(
                clientSecret,
                {
                    payment_method: {
                        card: cardElement,
                        billing_details: billingDetails
                    }
                }
            )

            if (error) {
                console.error('Erro no pagamento:', error.message)
                return { success: false, error: error.message }
            }

            console.log('Pagamento confirmado!', paymentIntent)
            return { success: true, paymentIntent }

        } catch (error) {
            console.error('Erro ao confirmar pagamento:', error)
            return { success: false, error: error.message }
        }
    }

    async handlePixPayment(clientSecret, billingDetails = {}) {
        try {
            console.log('📱 Processando pagamento PIX...')

            const stripe = await getStripe()

            // Para PIX, precisamos confirmar o PaymentIntent com o tipo de pagamento
            // Isso faz com que o Stripe gere os dados do QR Code no next_action
            const { error, paymentIntent } = await stripe.confirmPixPayment(clientSecret, {
                payment_method: {
                    billing_details: {
                        name: billingDetails.name || 'Customer',
                        email: billingDetails.email || 'customer@example.com'
                    }
                }
            })

            if (error) {
                console.error('Erro ao confirmar pagamento PIX:', error.message)
                return { success: false, error: error.message }
            }

            console.log('Pagamento PIX iniciado!', paymentIntent)
            console.log('Dados do QR Code:', paymentIntent.next_action?.pix_display_qr_code)
            return { success: true, paymentIntent }

        } catch (error) {
            console.error('Erro ao processar pagamento PIX:', error)
            return { success: false, error: error.message }
        }
    }

    async handleBoletoPayment(clientSecret, billingDetails = {}) {
        try {
            console.log('📄 Processando pagamento Boleto...')

            const stripe = await getStripe()

            // Para Boleto, precisamos confirmar o PaymentIntent com o tipo de pagamento
            // Isso faz com que o Stripe gere os dados do boleto no next_action
            const { error, paymentIntent } = await stripe.confirmBoletoPayment(clientSecret, {
                payment_method: {
                    billing_details: {
                        name: billingDetails.name || 'Customer',
                        email: billingDetails.email || 'customer@example.com'
                    }
                }
            })

            if (error) {
                console.error('Erro ao confirmar pagamento Boleto:', error.message)
                return { success: false, error: error.message }
            }

            console.log('Pagamento Boleto iniciado!', paymentIntent)
            console.log('Dados do Boleto:', paymentIntent.next_action?.boleto_display_details)
            return { success: true, paymentIntent }

        } catch (error) {
            console.error('Erro ao processar pagamento Boleto:', error)
            return { success: false, error: error.message }
        }
    }

    async checkPaymentStatus(paymentIntentId) {
        try {
            const response = await fetch(`${API_URL}/api/payments/${paymentIntentId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            })

            if (!response.ok) {
                throw new Error('Erro ao verificar status do pagamento')
            }

            const data = await response.json()
            return data

        } catch (error) {
            console.error('Erro ao verificar status:', error)
            throw error
        }
    }
}

export default new PaymentService()